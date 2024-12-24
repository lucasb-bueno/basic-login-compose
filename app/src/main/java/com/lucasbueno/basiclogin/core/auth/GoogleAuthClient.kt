package com.lucasbueno.basiclogin.core.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.presentation.login.LogInState
import com.lucasbueno.basiclogin.presentation.profile.ProfileState
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val googleSignInClient: SignInClient
) : AuthProvider {
    private val auth = Firebase.auth

    override suspend fun login(email: String?, password: String?): DataState<LogInState> {
        return DataState.Error(message = "GoogleAuth does not support direct email/password login")
    }

    suspend fun loginWithGoogle(): IntentSender? {
        val result = try {
            googleSignInClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): DataState<LogInState> {
        val credential = googleSignInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            DataState.Success(data = user?.run {
                LogInState(
                    userData = UserData(
                        userId = uid,
                        email = email.toString(),
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            DataState.Error(message = e.message.orEmpty())
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            googleSignInClient.signOut().await()
            auth.signOut()
        }.onFailure { e ->
            if (e is CancellationException) throw e // Rethrow cancellation exceptions
        }
    }

    fun getSignedInUser(): DataState<ProfileState> {
        return auth.currentUser?.run {
            DataState.Success(
                data = ProfileState(
                    userData = UserData(
                        userId = uid,
                        email = email.toString(),
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                )
            )
        } ?: DataState.Error(message = "Not able to get user data")
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

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}