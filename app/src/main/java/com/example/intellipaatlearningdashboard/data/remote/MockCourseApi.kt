package com.example.intellipaatlearningdashboard.data.remote

import android.content.Context
import com.example.intellipaatlearningdashboard.data.remote.dto.CourseDto
import com.example.intellipaatlearningdashboard.data.remote.dto.LessonDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock network API. Reads bundled JSON after a delay to simulate a real backend.
 * Flipping [forceFailure] lets you demo the dashboard error state in an interview.
 */
@Singleton
class MockCourseApi @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    var forceFailure: Boolean = false
    suspend fun fetchCourses(): List<CourseDto> {
        delay(NETWORK_DELAY_MS)
        if (forceFailure) {
            throw IOException("Unable to reach the courses service.")
        }
        val json = context.assets.open(ASSET_FILE).bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        return buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val id = obj.getInt("id")
                val title = obj.getString("title")
                val instructor = obj.getString("instructor")
                val progress = obj.getInt("progress")
                val lessonCount = obj.getInt("lessons")
                val lessons = buildLessons(id, title, lessonCount, progress)
                add(
                    CourseDto(
                        id = id,
                        title = title,
                        instructor = instructor,
                        progress = progress,
                        lessons = lessons,
                    ),
                )
            }
        }
    }

    private fun buildLessons(
        courseId: Int,
        courseTitle: String,
        count: Int,
        progress: Int,
    ): List<LessonDto> {
        val titles = lessonCatalog(courseTitle).padTo(count)
        val completedCount = ((progress / 100.0) * count).toInt().coerceIn(0, count)
        return titles.mapIndexed { index, name ->
            LessonDto(
                id = courseId * 1000 + index + 1,
                courseId = courseId,
                title = name,
                isCompleted = index < completedCount,
                sortOrder = index,
            )
        }
    }

    private fun lessonCatalog(courseTitle: String): List<String> = when {
        courseTitle.contains("Python", ignoreCase = true) -> listOf(
            "Introduction",
            "Variables & Data Types",
            "Functions",
            "OOP",
            "Control Flow",
            "Collections",
            "Modules & Packages",
            "File Handling",
            "Exceptions",
            "List Comprehensions",
            "Decorators",
            "Generators",
            "Virtual Environments",
            "NumPy Basics",
            "Pandas Basics",
            "Data Visualization",
            "APIs with Python",
            "Testing",
            "Mini Project",
            "Capstone",
        )
        courseTitle.contains("Generative AI", ignoreCase = true) -> listOf(
            "Introduction to GenAI",
            "LLMs Overview",
            "Prompt Engineering",
            "Tokens & Context",
            "Embeddings",
            "RAG Basics",
            "Fine-tuning Concepts",
            "Safety & Guardrails",
            "Image Generation",
            "Evaluation",
            "Agents",
            "Tool Calling",
            "Cost Optimization",
            "Production Patterns",
            "Mini Project",
            "Capstone",
        )
        else -> listOf(
            "HTML Fundamentals",
            "CSS Layouts",
            "JavaScript Basics",
            "DOM & Events",
            "ES6+",
            "Git & GitHub",
            "React Components",
            "State & Props",
            "React Hooks",
            "Routing",
            "REST APIs",
            "Node.js Intro",
            "Express.js",
            "Authentication",
            "SQL Basics",
            "MongoDB",
            "ORMs",
            "Testing",
            "CI/CD",
            "Deployment",
            "Performance",
            "Security",
            "TypeScript",
            "State Management",
            "WebSockets",
            "Caching",
            "Mini Project",
            "Capstone",
        )
    }

    private fun List<String>.padTo(count: Int): List<String> {
        if (size >= count) return take(count)
        return this + (size + 1..count).map { "Lesson $it" }
    }

    private companion object {
        const val ASSET_FILE = "courses.json"
        const val NETWORK_DELAY_MS = 900L
    }
}
