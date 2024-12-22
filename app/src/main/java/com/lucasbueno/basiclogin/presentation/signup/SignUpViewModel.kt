package com.lucasbueno.basiclogin.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.domain.model.SignUpModel
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow<DataState<Boolean>>(DataState.Success(false))
    val state = _state.asStateFlow()

    fun registerUser(
        signUpModel: SignUpModel,
        authClient: FirebaseAuthClient
    ) {
        viewModelScope.launch {
            _state.value = DataState.Loading
            when (val result =
                registerUserAuth(
                    signUpModel = signUpModel,
                    authClient = authClient
                )) {
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

    private suspend fun registerUserAuth(
        signUpModel: SignUpModel,
        authClient: FirebaseAuthClient
    ): DataState<Unit> {
        return authClient.registerUser(email = signUpModel.email, password = signUpModel.password).fold(
            onSuccess = { id ->
                if (id != null) {
                    userRepository.createUser(
                        user = UserData(
                            userId = id,
                            email = signUpModel.email,
                            userName = signUpModel.userName,
                            profilePictureUrl = null
                        )
                    ).fold(
                        onSuccess = {
                            DataState.Success(Unit)
                        },
                        onFailure = { error ->
                            DataState.Error(message = error.message.orEmpty())
                        }
                    )
                } else {
                    DataState.Error(message = "Error: id is Null")
                }
            },
            onFailure = { error ->
                DataState.Error(message = error.localizedMessage.orEmpty())
            }
        )
    }
}