package com.lucasbueno.basiclogin.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.lucasbueno.basiclogin.domain.model.UserData

interface UserRepository {
    suspend fun createUser(user: UserData): Result<Unit>
    suspend fun getUser(userId: String): Result<UserData>
    suspend fun getUserId(): Result<String>
    suspend fun getSignedInUser(): Result<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}