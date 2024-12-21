package com.lucasbueno.basiclogin.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

const val signUpRoute = "signUpRoute"

@Composable
fun SignUpRoute(
    authProvider: FirebaseAuthClient,
    onSignUpSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: SignUpViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreen(
        signUpState = state,
        onSignUpClick = { email, password ->
            viewModel.registerUser(email = email, password = password, authClient = authProvider)
        },
        onBackClick = onBackClick,
        onSuccessRegister = onSignUpSuccess
    )
}