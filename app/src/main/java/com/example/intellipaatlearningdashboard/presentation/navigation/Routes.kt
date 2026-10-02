package com.example.intellipaatlearningdashboard.presentation.navigation

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val COURSE_DETAILS = "course/{courseId}"

    fun courseDetails(courseId: Int) = "course/$courseId"
}
