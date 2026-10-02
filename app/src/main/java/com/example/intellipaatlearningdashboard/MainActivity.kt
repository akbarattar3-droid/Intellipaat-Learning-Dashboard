package com.example.intellipaatlearningdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.intellipaatlearningdashboard.domain.repository.AuthRepository
import com.example.intellipaatlearningdashboard.presentation.navigation.AppNavHost
import com.example.intellipaatlearningdashboard.presentation.navigation.Routes
import com.example.intellipaatlearningdashboard.ui.theme.IntellipaatLearningDashboardTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val startDestination = if (authRepository.isLoggedIn()) {
            Routes.DASHBOARD
        } else {
            Routes.LOGIN
        }
        setContent {
            IntellipaatLearningDashboardTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                ) {
                    AppNavHost(startDestination = startDestination)
                }
            }
        }
    }
}
