package com.lucasbueno.basiclogin.presentation.signup

import androidx.lifecycle.ViewModel
import com.lucasbueno.basiclogin.domain.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel: ViewModel() {
    private val _state = MutableStateFlow<DataState<Boolean>>(DataState.Loading)
    val state = _state.asStateFlow()

    fun updateState(state: DataState<Boolean>) {
        _state.value = state
    }
}