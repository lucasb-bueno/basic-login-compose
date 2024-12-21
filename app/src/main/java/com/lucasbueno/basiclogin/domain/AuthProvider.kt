package com.lucasbueno.basiclogin.domain

import com.lucasbueno.basiclogin.presentation.signin.LogInState

interface AuthProvider {
    suspend fun login(email: String?, password: String?): DataState<LogInState>?
    suspend fun logout(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
}