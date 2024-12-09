package com.lucasbueno.basiclogin.presentation.signin

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.domain.AuthProvider
import com.lucasbueno.basiclogin.domain.DataState
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val googleSignInClient: SignInClient
): AuthProvider {
    private val auth = Firebase.auth

    override suspend fun login(email: String?, password: String?): DataState<LogInState> {
        return DataState.Error(message = "GoogleAuth does not support direct email/password login")
    }

    suspend fun loginWithGoogle(): IntentSender? {
        val result = try {
            googleSignInClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
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
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                )
            })
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            DataState.Error(message = e.message.orEmpty())
        }
    }

    override suspend fun logout() {
        try {
            googleSignInClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
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