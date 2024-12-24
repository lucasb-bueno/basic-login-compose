package com.lucasbueno.basiclogin.core

interface AuthProvider {
    suspend fun login(email: String?, password: String?): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getLoggedInUserId(): Result<String>
}