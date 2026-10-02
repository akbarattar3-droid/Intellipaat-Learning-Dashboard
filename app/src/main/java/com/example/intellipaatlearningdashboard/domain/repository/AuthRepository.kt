package com.example.intellipaatlearningdashboard.domain.repository

import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<AppResult<Unit>>
    fun isLoggedIn(): Boolean
}
