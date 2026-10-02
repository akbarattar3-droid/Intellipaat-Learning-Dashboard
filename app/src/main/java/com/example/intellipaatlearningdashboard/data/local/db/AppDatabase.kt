package com.example.intellipaatlearningdashboard.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.intellipaatlearningdashboard.data.local.dao.CourseDao
import com.example.intellipaatlearningdashboard.data.local.dao.LessonDao
import com.example.intellipaatlearningdashboard.data.local.entity.CourseEntity
import com.example.intellipaatlearningdashboard.data.local.entity.LessonEntity

@Database(
    entities = [CourseEntity::class, LessonEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun lessonDao(): LessonDao
}
