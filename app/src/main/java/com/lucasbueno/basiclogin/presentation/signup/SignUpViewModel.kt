package com.lucasbueno.basiclogin.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.domain.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    private val _state = MutableStateFlow<DataState<Boolean>>(DataState.Success(false))
    val state = _state.asStateFlow()

    fun registerUser(email: String, password: String, authClient: FirebaseAuthClient) {
        viewModelScope.launch {
            _state.value = DataState.Loading
            when (val result = authClient.registerUser(email.trim(), password)) {
                is DataState.Success -> {
                    _state.value = DataState.Success(true)
                }
                is DataState.Error -> {
                    _state.value = DataState.Error(result.message)
                }
                else -> Unit
            }
        }
    }
}