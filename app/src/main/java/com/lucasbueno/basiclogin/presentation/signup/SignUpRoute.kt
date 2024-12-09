package com.lucasbueno.basiclogin.presentation.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignUpRoute(
    onSignUpClick: (String, String) -> Unit,
) {
    val viewModel: SignUpViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreen(
        onSignUpClick = onSignUpClick
    )
}