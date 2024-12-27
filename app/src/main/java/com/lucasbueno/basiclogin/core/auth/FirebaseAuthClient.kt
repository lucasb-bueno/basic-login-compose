package com.lucasbueno.basiclogin.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.presentation.profile.ProfileState
import kotlinx.coroutines.tasks.await

class FirebaseAuthClient : AuthProvider {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String?, password: String?): Result<Unit> {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return Result.failure(exception = Throwable(message = "Email and password cannot be empty"))
        }

        return try {
            auth.signInWithEmailAndPassword(email, password).await()?.let {
                Result.success(Unit)
            } ?: Result.failure(exception = Throwable())
        } catch (e: Exception) {
            Result.failure(exception = Throwable(message = e.localizedMessage.orEmpty()))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            auth.signOut()
        }.onFailure { e ->
            DataState.Error(message = e.localizedMessage ?: "Error on Logout")
        }.onSuccess {
            DataState.Success(ProfileState(shouldLogOut = true))
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getLoggedInUserId(): Result<String> {
        return try {
            auth.currentUser?.uid?.let {
                Result.success(it)
            } ?: Result.failure(exception = Throwable())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSignedInUser(): Result<FirebaseUser> {
        return auth.currentUser?.let {
            Result.success(it)
        } ?: Result.failure(exception = Throwable(message = "No current user Logged in"))
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(exception = Throwable(message = e.localizedMessage))
        }
    }

    suspend fun registerUser(
        email: String,
        password: String
    ): Result<String?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }
}
