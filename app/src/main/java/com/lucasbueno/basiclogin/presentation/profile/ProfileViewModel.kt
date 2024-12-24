package com.lucasbueno.basiclogin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.core.auth.GoogleAuthUiClient
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<DataState<ProfileState>>(DataState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        fetchData()
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

    fun fetchData() {
        _profileState.update { DataState.Loading }
        viewModelScope.launch {
            userRepository.getUserId().fold(
                onSuccess = { id ->
                    userRepository.getUser(id).fold(
                        onSuccess = { userData ->
                            _profileState.update {
                                DataState.Success(
                                    data = ProfileState(
                                        userData = userData
                                    )
                                )
                            }
                        },
                        onFailure = { error ->
                            _profileState.update {
                                DataState.Error(message = "Error fetching data from Firestore: ${error.localizedMessage}")
                            }
                        }
                    )
                },
                onFailure = { error ->
                    _profileState.update {
                        DataState.Error(message = "Error fetching Id: ${error.localizedMessage}")
                    }
                }
            )
        }
    }
}

data class ProfileState(
    val userData: UserData? = null,
    val shouldLogOut: Boolean = false
)