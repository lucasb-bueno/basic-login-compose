package com.lucasbueno.basiclogin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.domain.DatabaseService
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.presentation.signin.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val databaseService: DatabaseService
) : ViewModel() {
    private val _profileState = MutableStateFlow<DataState<ProfileState>>(DataState.Loading)
    val profileState = _profileState.asStateFlow()

    fun onProfileResult(result: DataState<ProfileState>) {
        _profileState.value = result
    }

    fun logout(googleAuthUiClient: GoogleAuthUiClient) {
        viewModelScope.launch {
            val result = googleAuthUiClient.logout()
            result.fold(
                onSuccess = {
                    _profileState.update {
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