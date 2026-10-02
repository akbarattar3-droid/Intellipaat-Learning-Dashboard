package com.example.intellipaatlearningdashboard.data.local.session

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply()
    }

    private companion object {
        const val PREFS_NAME = "learning_session"
        const val KEY_LOGGED_IN = "logged_in"
    }
}
