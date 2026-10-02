package com.example.intellipaatlearningdashboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.intellipaatlearningdashboard.data.local.entity.CourseEntity
import com.example.intellipaatlearningdashboard.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    suspend fun getAll(): List<CourseEntity>

    @Query("SELECT * FROM courses")
    fun observeAll(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getById(id: Int): CourseEntity?

    @Query("SELECT * FROM courses WHERE id = :id")
    fun observeById(id: Int): Flow<CourseEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(courses: List<CourseEntity>)

    @Query("DELETE FROM courses")
    suspend fun clear()

    @Query("UPDATE courses SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: Int, progress: Int)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY sortOrder ASC")
    suspend fun getByCourseId(courseId: Int): List<LessonEntity>

    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY sortOrder ASC")
    fun observeByCourseId(courseId: Int): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(lessons: List<LessonEntity>)

    @Query("DELETE FROM lessons")
    suspend fun clear()

    @Query("UPDATE lessons SET isCompleted = 1 WHERE id = :lessonId")
    suspend fun markCompleted(lessonId: Int)

    @Query("SELECT COUNT(*) FROM lessons WHERE courseId = :courseId AND isCompleted = 1")
    suspend fun completedCount(courseId: Int): Int

    @Query("SELECT COUNT(*) FROM lessons WHERE courseId = :courseId")
    suspend fun totalCount(courseId: Int): Int
}
