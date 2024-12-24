package com.lucasbueno.basiclogin.presentation.login

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.core.auth.FirebaseAuthClient
import com.lucasbueno.basiclogin.core.auth.GoogleAuthUiClient
import com.lucasbueno.basiclogin.domain.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val firebaseAuthProvider: FirebaseAuthClient,
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<LogInState>>(DataState.Default)
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<LoginUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        getSignedUser()
    }

    private fun getSignedUser() {
        viewModelScope.launch {
            googleAuthUiClient.getSignedInUser().fold(
                onSuccess = {
                    _state.update { DataState.Success(LogInState(userData = null)) }
                },
                onFailure = {
                    _state.update { DataState.Default }
                }
            )
        }
    }

    fun handleGoogleLogin() {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.loginWithGoogle()
            if (signInIntentSender != null) {
                _uiEvents.emit(LoginUiEvent.GoogleSignIn(signInIntentSender))
            } else {
                _state.update { DataState.Error("Failed to get Google Sign-In intent.") }
            }
        }
    }

    fun onGoogleLoginResult(data: Intent?) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            googleAuthUiClient.signInWithIntent(data ?: return@launch).fold(
                onSuccess = {
                    _state.update { DataState.Success(data = LogInState()) }
                },
                onFailure = { error ->
                    _state.update {
                        DataState.Error(
                            message = error.localizedMessage ?: "Error on google Login"
                        )
                    }
                }
            )
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            firebaseAuthProvider.login(email, password).fold(
                onSuccess = {
                    _state.update { DataState.Success(LogInState()) }
                },
                onFailure = { error ->
                    _state.update {
                        DataState.Error(
                            message = error.localizedMessage ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    fun resetLoginState() {
        _state.update { DataState.Default }
    }
}

data class LogInState(
    val userData: UserData? = null,
    val errorMessage: String? = null
)

sealed class LoginUiEvent {
    data class GoogleSignIn(val intentSender: IntentSender) : LoginUiEvent()
}