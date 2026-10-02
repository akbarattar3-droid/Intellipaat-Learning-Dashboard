package com.example.intellipaatlearningdashboard.domain.usecase

import com.example.intellipaatlearningdashboard.domain.model.CourseDetails
import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCourseDetailsUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    operator fun invoke(courseId: Int): Flow<AppResult<CourseDetails>> {
        return courseRepository.observeCourseDetails(courseId)
    }
}
