package com.lucasbueno.basiclogin.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.domain.AuthProvider
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.domain.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow<DataState<LogInState>>(DataState.Default)
    val state = _state.asStateFlow()

    fun onSignInResult(result: DataState<LogInState>) {
        _state.update { result }
    }

    fun resetState() {
        _state.update { DataState.Default }
    }

    fun login(authProvider: AuthProvider, email: String, password: String) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            val result = authProvider.login(email, password)
            result?.let { onSignInResult(it) }
        }
    }
}

data class LogInState(
    val userData: UserData? = null,
    val errorMessage: String? = null
)