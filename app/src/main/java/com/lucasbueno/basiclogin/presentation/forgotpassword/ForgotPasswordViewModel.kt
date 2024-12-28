package com.lucasbueno.basiclogin.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<Unit>>(DataState.Default)
    val state = _state.asStateFlow()

    fun resetPassword(email: String) {
        _state.update { DataState.Loading }
        viewModelScope.launch {
            userRepository.sendPasswordResetEmail(email).fold(
                onSuccess = {
                    _state.update { DataState.Success(Unit) }
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
}