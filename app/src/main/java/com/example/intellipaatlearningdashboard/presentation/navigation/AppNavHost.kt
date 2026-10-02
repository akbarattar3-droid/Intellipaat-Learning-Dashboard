package com.example.intellipaatlearningdashboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.intellipaatlearningdashboard.presentation.dashboard.CourseDashboardScreen
import com.example.intellipaatlearningdashboard.presentation.dashboard.DashboardViewModel
import com.example.intellipaatlearningdashboard.presentation.details.CourseDetailsScreen
import com.example.intellipaatlearningdashboard.presentation.details.CourseDetailsViewModel
import com.example.intellipaatlearningdashboard.presentation.login.LoginScreen
import com.example.intellipaatlearningdashboard.presentation.login.LoginViewModel

@Composable
fun AppNavHost(
    startDestination: String,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            CourseDashboardScreen(
                viewModel = viewModel,
                onCourseClick = { courseId ->
                    navController.navigate(Routes.courseDetails(courseId))
                },
            )
        }
        composable(
            route = Routes.COURSE_DETAILS,
            arguments = listOf(navArgument("courseId") { type = NavType.IntType }),
        ) {
            val viewModel: CourseDetailsViewModel = hiltViewModel()
            CourseDetailsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
