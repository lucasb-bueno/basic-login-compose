package com.lucasbueno.basiclogin.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

const val signUpRoute = "signUpRoute"

@Composable
fun SignUpRoute(
    onSignUpSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val signUpState by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreen(
        signUpState = signUpState,
        onSignUpClick = { signUpModel ->
            viewModel.registerUser(signUpModel = signUpModel)
        },
        onBackClick = onBackClick,
        onSuccessRegister = onSignUpSuccess
    )
}