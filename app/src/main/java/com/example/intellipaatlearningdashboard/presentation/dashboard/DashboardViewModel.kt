package com.example.intellipaatlearningdashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import com.example.intellipaatlearningdashboard.domain.usecase.GetCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadCourses()
    }

    fun loadCourses(showLoading: Boolean = true) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            if (showLoading && _uiState.value !is DashboardUiState.Success) {
                _uiState.value = DashboardUiState.Loading
            }
            getCoursesUseCase().collect { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> {
                        if (result.data.isEmpty()) DashboardUiState.Empty
                        else DashboardUiState.Success(result.data)
                    }
                    is AppResult.Error -> DashboardUiState.Error(result.message)
                }
            }
        }
    }
}
