package com.lucasbueno.basiclogin.presentation.signin

import androidx.lifecycle.ViewModel
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.domain.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow<DataState<LogInState>>(DataState.Loading)
    val state = _state.asStateFlow()

    fun onSignInResult(result: DataState<LogInState>) {
        _state.update { result }
    }

    fun resetState() {
        _state.update { DataState.Loading }
    }
}

data class LogInState(
    val userData: UserData? = null,
    val errorMessage: String? = null
)