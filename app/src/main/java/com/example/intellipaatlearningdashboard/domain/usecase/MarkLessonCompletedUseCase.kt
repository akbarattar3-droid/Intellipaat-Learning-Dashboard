package com.example.intellipaatlearningdashboard.domain.usecase

import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarkLessonCompletedUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    operator fun invoke(courseId: Int, lessonId: Int): Flow<AppResult<Unit>> {
        return courseRepository.markLessonCompleted(courseId, lessonId)
    }
}
