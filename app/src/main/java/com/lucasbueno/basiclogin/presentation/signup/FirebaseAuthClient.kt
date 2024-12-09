package com.lucasbueno.basiclogin.presentation.signup

import com.google.firebase.auth.FirebaseAuth
import com.lucasbueno.basiclogin.domain.AuthProvider
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.presentation.signin.LogInState
import com.lucasbueno.basiclogin.presentation.signin.UserData

class FirebaseAuthClient : AuthProvider {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String?, password: String?): DataState<LogInState>? {
        var result: DataState<LogInState>? = null

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return DataState.Error(message = "Email and password cannot be empty")
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result = DataState.Success(
                        data = LogInState(
                            userData = UserData(
                                userId = auth.currentUser?.uid.orEmpty(),
                                username = auth.currentUser?.displayName,
                                profilePictureUrl = auth.currentUser?.photoUrl?.toString()
                            )
                        )
                    )
                } else {
                    result = DataState.Error(
                        message = task.exception?.localizedMessage.orEmpty()
                    )
                }
            }
        return result
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun registerUser(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.localizedMessage.orEmpty())
                }
            }
    }
}
