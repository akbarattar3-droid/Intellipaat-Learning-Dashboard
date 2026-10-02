package com.example.intellipaatlearningdashboard.domain.model

object CourseProgressCalculator {
    fun percent(completedCount: Int, totalCount: Int): Int {
        if (totalCount <= 0) return 0
        return (completedCount * 100) / totalCount
    }

    fun fromLessons(lessons: List<Lesson>): Int {
        return percent(
            completedCount = lessons.count { it.isCompleted },
            totalCount = lessons.size,
        )
    }
}
