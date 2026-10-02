package com.example.intellipaatlearningdashboard.domain.usecase

import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    operator fun invoke(): Flow<AppResult<List<Course>>> = courseRepository.observeCourses()
}
