package com.example.intellipaatlearningdashboard.data.remote

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthApi @Inject constructor() {
    suspend fun login(email: String, password: String): Boolean {
        delay(NETWORK_DELAY_MS)
        return email.equals(VALID_EMAIL, ignoreCase = true) && password == VALID_PASSWORD
    }

    companion object {
        const val VALID_EMAIL = "user@intellipaat.com"
        const val VALID_PASSWORD = "password123"
        private const val NETWORK_DELAY_MS = 800L
    }
}
