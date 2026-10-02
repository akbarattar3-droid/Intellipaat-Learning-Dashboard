package com.example.intellipaatlearningdashboard.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.intellipaatlearningdashboard.domain.model.Course
import com.example.intellipaatlearningdashboard.presentation.common.EmptyState
import com.example.intellipaatlearningdashboard.presentation.common.ErrorState
import com.example.intellipaatlearningdashboard.presentation.common.FullScreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDashboardScreen(
    viewModel: DashboardViewModel,
    onCourseClick: (Int) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Course Dashboard") })
        },
    ) { innerPadding ->
        when (val current = state) {
            DashboardUiState.Loading -> FullScreenLoading(Modifier.padding(innerPadding))
            DashboardUiState.Empty -> EmptyState(Modifier.padding(innerPadding))
            is DashboardUiState.Error -> ErrorState(
                message = current.message,
                onRetry = { viewModel.loadCourses() },
                modifier = Modifier.padding(innerPadding),
            )
            is DashboardUiState.Success -> CourseList(
                courses = current.courses,
                onCourseClick = onCourseClick,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun CourseList(
    courses: List<Course>,
    onCourseClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(courses, key = { it.id }) { course ->
            CourseCard(course = course, onContinue = { onCourseClick(course.id) })
        }
    }
}

@Composable
private fun CourseCard(
    course: Course,
    onContinue: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onContinue),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = course.instructor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LinearProgressIndicator(
                    progress = { course.progress / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                )
                Text(
                    text = "${course.progress}%",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${course.lessonCount} lessons",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Continue")
            }
        }
    }
}
