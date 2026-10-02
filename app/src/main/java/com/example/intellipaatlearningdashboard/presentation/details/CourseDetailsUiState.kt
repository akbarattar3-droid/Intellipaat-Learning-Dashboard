package com.example.intellipaatlearningdashboard.presentation.details

import com.example.intellipaatlearningdashboard.domain.model.CourseDetails

sealed interface CourseDetailsUiState {
    data object Loading : CourseDetailsUiState
    data class Success(val details: CourseDetails) : CourseDetailsUiState
    data class Error(val message: String) : CourseDetailsUiState
}
