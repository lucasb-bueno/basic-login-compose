package com.lucasbueno.basiclogin.presentation.signup

import com.google.firebase.auth.FirebaseAuth
import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.presentation.signin.LogInState
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthClient : AuthProvider {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String?, password: String?): DataState<LogInState> {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return DataState.Error(message = "Email and password cannot be empty")
        }

        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            DataState.Success(
                data = LogInState(
                    userData = null
                )
            )
        } catch (e: Exception) {
            DataState.Error(
                message = e.localizedMessage.orEmpty()
            )
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            auth.signOut()
        }.onFailure { e ->
            if (e is CancellationException) throw e // Rethrow cancellation exceptions
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
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
