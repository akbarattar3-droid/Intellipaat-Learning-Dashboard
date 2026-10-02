package com.example.intellipaatlearningdashboard.data.repository

import com.example.intellipaatlearningdashboard.data.local.session.SessionLocalDataSource
import com.example.intellipaatlearningdashboard.data.remote.MockAuthApi
import com.example.intellipaatlearningdashboard.domain.repository.AuthRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val mockAuthApi: MockAuthApi,
    private val sessionLocalDataSource: SessionLocalDataSource,
) : AuthRepository {

    override fun login(email: String, password: String): Flow<AppResult<Unit>> = flow {
        try {
            val success = mockAuthApi.login(email, password)
            if (success) {
                sessionLocalDataSource.setLoggedIn(true)
                emit(AppResult.Success(Unit))
            } else {
                emit(AppResult.Error("Invalid email or password."))
            }
        } catch (error: Exception) {
            emit(AppResult.Error(error.message ?: "Login failed. Please try again."))
        }
    }

    override fun isLoggedIn(): Boolean = sessionLocalDataSource.isLoggedIn()
}
