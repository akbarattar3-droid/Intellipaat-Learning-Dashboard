package com.example.intellipaatlearningdashboard.domain.repository

import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.domain.model.CourseDetails
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun observeCourses(): Flow<AppResult<List<Course>>>
    fun observeCourseDetails(courseId: Int): Flow<AppResult<CourseDetails>>
    fun markLessonCompleted(courseId: Int, lessonId: Int): Flow<AppResult<Unit>>
}
