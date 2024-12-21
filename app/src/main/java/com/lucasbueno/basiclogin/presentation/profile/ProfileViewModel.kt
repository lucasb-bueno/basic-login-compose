package com.lucasbueno.basiclogin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.presentation.signin.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _state = MutableStateFlow<DataState<ProfileState>>(DataState.Loading)
    val state = _state.asStateFlow()

    fun onProfileResult(result: DataState<ProfileState>) {
        _state.value = result
    }

    fun logout(googleAuthUiClient: GoogleAuthUiClient) {
        viewModelScope.launch {
            val result = googleAuthUiClient.logout()
            result.fold(
                onSuccess = {
                    _state.update {
                        DataState.Success(data = ProfileState(shouldLogOut = true))
                    }
                },
                onFailure = { error ->
                    //TODO: Add Error Scenario
                    println("Logout failed: ${error.message}")
                }
            )
        }
    }
}

data class ProfileState(
    val userData: UserData? = null,
    val shouldLogOut: Boolean = false
)