package com.example.intellipaatlearningdashboard.data.local.mapper

import com.example.intellipaatlearningdashboard.data.local.entity.CourseEntity
import com.example.intellipaatlearningdashboard.data.local.entity.LessonEntity
import com.example.intellipaatlearningdashboard.data.remote.dto.CourseDto
import com.example.intellipaatlearningdashboard.data.remote.dto.LessonDto
import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.domain.model.CourseDetails
import com.example.intellipaatlearningdashboard.domain.model.Lesson

fun CourseEntity.toDomain() = Course(
    id = id,
    title = title,
    instructor = instructor,
    progress = progress,
    lessonCount = lessonCount,
)

fun LessonEntity.toDomain() = Lesson(
    id = id,
    title = title,
    courseId = courseId,
    isCompleted = isCompleted,
    sortOrder = sortOrder,
)

fun CourseDto.toEntity() = CourseEntity(
    id = id,
    title = title,
    instructor = instructor,
    progress = progress,
    lessonCount = lessons.size,
)

fun LessonDto.toEntity() = LessonEntity(
    id = id,
    courseId = courseId,
    title = title,
    isCompleted = isCompleted,
    sortOrder = sortOrder,
)

fun toCourseDetails(course: CourseEntity, lessons: List<LessonEntity>): CourseDetails {
    return CourseDetails(
        course = course.toDomain(),
        lessons = lessons.map { it.toDomain() },
    )
}
