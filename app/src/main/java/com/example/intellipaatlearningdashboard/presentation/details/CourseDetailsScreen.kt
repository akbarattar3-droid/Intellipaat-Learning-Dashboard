package com.example.intellipaatlearningdashboard.presentation.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.intellipaatlearningdashboard.domain.model.Lesson
import com.example.intellipaatlearningdashboard.presentation.common.ErrorState
import com.example.intellipaatlearningdashboard.presentation.common.FullScreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    viewModel: CourseDetailsViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (state as? CourseDetailsUiState.Success)?.details?.course?.title
                        ?: "Course details"
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        when (val current = state) {
            CourseDetailsUiState.Loading -> FullScreenLoading(Modifier.padding(innerPadding))
            is CourseDetailsUiState.Error -> ErrorState(
                message = current.message,
                onRetry = viewModel::loadDetails,
                modifier = Modifier.padding(innerPadding),
            )
            is CourseDetailsUiState.Success -> DetailsContent(
                details = current.details.course.title,
                progress = current.details.course.progress,
                lessons = current.details.lessons,
                onLessonClick = viewModel::onMarkLessonCompleted,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun DetailsContent(
    details: String,
    progress: Int,
    lessons: List<Lesson>,
    onLessonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = details, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Current progress  $progress%",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Lessons", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(lessons, key = { it.id }) { lesson ->
                LessonRow(
                    lesson = lesson,
                    onClick = { if (!lesson.isCompleted) onLessonClick(lesson.id) },
                )
            }
        }
    }
}

@Composable
private fun LessonRow(
    lesson: Lesson,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !lesson.isCompleted, onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (lesson.isCompleted) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Completed",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Completed",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.RadioButtonUnchecked,
                contentDescription = "Pending",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Pending",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
