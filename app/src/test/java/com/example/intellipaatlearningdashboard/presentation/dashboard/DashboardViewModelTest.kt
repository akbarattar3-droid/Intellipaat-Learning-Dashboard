package com.example.intellipaatlearningdashboard.presentation.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import com.example.intellipaatlearningdashboard.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var courseRepository: CourseRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadCourses_mapsRepositoryFlowToSuccessUiState() = runTest {
        val courses = listOf(
            Course(
                id = 1,
                title = "Android Architecture",
                instructor = "Intellipaat",
                progress = 40,
                lessonCount = 8,
            ),
        )
        whenever(courseRepository.observeCourses()).thenReturn(
            flowOf(AppResult.Success(courses)),
        )

        val viewModel = DashboardViewModel(GetCoursesUseCase(courseRepository))

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(courses, (state as DashboardUiState.Success).courses)
        verify(courseRepository).observeCourses()
    }
}
