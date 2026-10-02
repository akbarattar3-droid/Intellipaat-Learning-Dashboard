package com.example.intellipaatlearningdashboard.di

import android.content.Context
import androidx.room.Room
import com.example.intellipaatlearningdashboard.data.local.dao.CourseDao
import com.example.intellipaatlearningdashboard.data.local.dao.LessonDao
import com.example.intellipaatlearningdashboard.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "learning_dashboard.db",
        ).build()
    }

    @Provides
    fun provideCourseDao(database: AppDatabase): CourseDao = database.courseDao()

    @Provides
    fun provideLessonDao(database: AppDatabase): LessonDao = database.lessonDao()
}
