package com.example.intellipaatlearningdashboard.di

import com.example.intellipaatlearningdashboard.data.repository.AuthRepositoryImpl
import com.example.intellipaatlearningdashboard.data.repository.CourseRepositoryImpl
import com.example.intellipaatlearningdashboard.domain.repository.AuthRepository
import com.example.intellipaatlearningdashboard.domain.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository
}
