package com.lucasbueno.basiclogin.core.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.presentation.profile.ProfileState
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val googleSignInClient: SignInClient
) : AuthProvider {
    private val auth = Firebase.auth

    override suspend fun login(email: String?, password: String?): Result<Unit> {
        return Result.failure(exception = Throwable(message = "GoogleAuth does not support direct email/password login"))
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

    suspend fun signInWithIntent(intent: Intent): Result<Unit> {
        val credential = googleSignInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            auth.signInWithCredential(googleCredentials).await()?.let {
                Result.success(Unit)
            } ?: Result.failure(exception = Throwable())
        } catch (e: Exception) {
            Result.failure(Throwable(message = e.localizedMessage))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            googleSignInClient.signOut().await()
            auth.signOut()
        }.onFailure { e ->
            DataState.Error(message = e.localizedMessage ?: "Error on Logout")
//            if (e is CancellationException) throw e
        }.onSuccess {
            DataState.Success(ProfileState(shouldLogOut = true))
        }
    }

    override suspend fun getSignedInUser(): Result<FirebaseUser> {
        return auth.currentUser?.let {
            Result.success(it)
        } ?: Result.failure(exception = Throwable(message = "No current user Logged in"))
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