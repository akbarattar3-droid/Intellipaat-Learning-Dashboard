package com.example.intellipaatlearningdashboard.domain.usecase

import com.example.intellipaatlearningdashboard.domain.repository.AuthRepository
import com.example.intellipaatlearningdashboard.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(email: String, password: String): Flow<AppResult<Unit>> {
        return authRepository.login(email.trim(), password)
    }
}
