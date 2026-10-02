package com.example.intellipaatlearningdashboard.data.repository

import androidx.room.withTransaction
import com.example.intellipaatlearningdashboard.data.local.dao.CourseDao
import com.example.intellipaatlearningdashboard.data.local.dao.LessonDao
import com.example.intellipaatlearningdashboard.data.local.db.AppDatabase
import com.example.intellipaatlearningdashboard.data.local.mapper.toCourseDetails
import com.example.intellipaatlearningdashboard.data.local.mapper.toDomain
import com.example.intellipaatlearningdashboard.data.local.mapper.toEntity
import com.example.intellipaatlearningdashboard.data.network.NetworkMonitor
import com.example.intellipaatlearningdashboard.data.remote.MockCourseApi
import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.domain.model.CourseDetails
import com.example.intellipaatlearningdashboard.domain.model.CourseProgressCalculator
import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val api: MockCourseApi,
    private val database: AppDatabase,
    private val courseDao: CourseDao,
    private val lessonDao: LessonDao,
    private val networkMonitor: NetworkMonitor,
) : CourseRepository {

    override fun observeCourses(): Flow<AppResult<List<Course>>> = flow {
        val cached = courseDao.getAll()
        if (cached.isEmpty()) {
            if (!networkMonitor.isOnline()) {
                emit(AppResult.Error("You're offline. Connect once to download courses."))
                return@flow
            }
            try {
                cacheRemoteCourses()
            } catch (error: Exception) {
                emit(AppResult.Error(error.message ?: "Couldn't load courses. Please try again."))
                return@flow
            }
        }
        emitAll(
            courseDao.observeAll().map { courses ->
                AppResult.Success(courses.map { it.toDomain() })
            },
        )
    }

    override fun observeCourseDetails(courseId: Int): Flow<AppResult<CourseDetails>> = flow {
        if (courseDao.getById(courseId) == null) {
            if (!networkMonitor.isOnline()) {
                emit(AppResult.Error("You're offline and this course isn't cached yet."))
                return@flow
            }
            try {
                cacheRemoteCourses()
            } catch (error: Exception) {
                emit(AppResult.Error(error.message ?: "Couldn't load course details."))
                return@flow
            }
            if (courseDao.getById(courseId) == null) {
                emit(AppResult.Error("Course not found."))
                return@flow
            }
        }
        emitAll(
            combine(
                courseDao.observeById(courseId),
                lessonDao.observeByCourseId(courseId),
            ) { course, lessons ->
                if (course == null) {
                    AppResult.Error("Course not found.")
                } else {
                    AppResult.Success(toCourseDetails(course, lessons))
                }
            },
        )
    }

    override fun markLessonCompleted(courseId: Int, lessonId: Int): Flow<AppResult<Unit>> = flow {
        try {
            lessonDao.markCompleted(lessonId)
            val lessons = lessonDao.getByCourseId(courseId).map { it.toDomain() }
            courseDao.updateProgress(courseId, CourseProgressCalculator.fromLessons(lessons))
            emit(AppResult.Success(Unit))
        } catch (error: Exception) {
            emit(AppResult.Error(error.message ?: "Couldn't update lesson progress."))
        }
    }

    private suspend fun cacheRemoteCourses() {
        val remote = api.fetchCourses()
        database.withTransaction {
            courseDao.clear()
            lessonDao.clear()
            courseDao.upsertAll(remote.map { it.toEntity() })
            lessonDao.upsertAll(remote.flatMap { course -> course.lessons.map { it.toEntity() } })
        }
    }
}
