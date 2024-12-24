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

    fun handleGoogleLogin() {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.loginWithGoogle()
            if (signInIntentSender != null) {
                _state.update { DataState.Success(LogInState(userData = null)) }
                _uiEvents.emit(LoginUiEvent.GoogleSignIn(signInIntentSender))
            } else {
                _state.update { DataState.Error("Failed to get Google Sign-In intent.") }
            }
        }
    }

    fun onGoogleLoginResult(data: Intent?) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            val logInState = googleAuthUiClient.signInWithIntent(data ?: return@launch)
            _state.update { logInState }
            if (logInState is DataState.Success && logInState.data?.userData != null) {
                _uiEvents.emit(LoginUiEvent.NavigateToProfile)
            } else if (logInState is DataState.Error) {
                _state.update { logInState }
            }
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            firebaseAuthProvider.login(email, password)?.let { result ->
                if (result is DataState.Success) {
                    _uiEvents.emit(LoginUiEvent.NavigateToProfile)
                }
                _state.update { result }
            }
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiEvents.emit(LoginUiEvent.NavigateToSignUp)
        }
    }
}

data class LogInState(
    val userData: UserData? = null,
    val errorMessage: String? = null
)

sealed class LoginUiEvent {
    data object NavigateToProfile : LoginUiEvent()
    data object NavigateToSignUp : LoginUiEvent()
    data class GoogleSignIn(val intentSender: IntentSender) : LoginUiEvent()
}