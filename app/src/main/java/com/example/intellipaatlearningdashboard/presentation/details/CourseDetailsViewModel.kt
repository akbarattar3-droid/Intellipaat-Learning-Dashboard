package com.example.intellipaatlearningdashboard.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import com.example.intellipaatlearningdashboard.domain.usecase.GetCourseDetailsUseCase
import com.example.intellipaatlearningdashboard.domain.usecase.MarkLessonCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailsUseCase: GetCourseDetailsUseCase,
    private val markLessonCompletedUseCase: MarkLessonCompletedUseCase,
) : ViewModel() {

    private val courseId: Int = checkNotNull(savedStateHandle["courseId"])

    private val _uiState = MutableStateFlow<CourseDetailsUiState>(CourseDetailsUiState.Loading)
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadDetails()
    }

    fun loadDetails() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            _uiState.value = CourseDetailsUiState.Loading
            getCourseDetailsUseCase(courseId).collect { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> CourseDetailsUiState.Success(result.data)
                    is AppResult.Error -> CourseDetailsUiState.Error(result.message)
                }
            }
        }
    }

    fun onMarkLessonCompleted(lessonId: Int) {
        viewModelScope.launch {
            markLessonCompletedUseCase(courseId, lessonId).collect { result ->
                if (result is AppResult.Error) {
                    _uiState.value = CourseDetailsUiState.Error(result.message)
                }
            }
        }
    }
}
