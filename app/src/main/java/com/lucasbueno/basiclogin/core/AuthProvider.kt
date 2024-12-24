package com.lucasbueno.basiclogin.core

import com.google.firebase.auth.FirebaseUser

interface AuthProvider {
    suspend fun login(email: String?, password: String?): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getLoggedInUserId(): Result<String>
    suspend fun getSignedInUser(): Result<FirebaseUser>
}