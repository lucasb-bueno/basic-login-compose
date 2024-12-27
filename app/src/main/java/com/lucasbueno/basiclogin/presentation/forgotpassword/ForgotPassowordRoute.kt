package com.lucasbueno.basiclogin.presentation.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

const val forgotPasswordRoute = "forgotPasswordRoute"

@Composable
fun ForgotPasswordRoute(
    navigateToLogin: () -> Unit
) {
    val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
    val uiState by forgotPasswordViewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = uiState,
        onBackClick = navigateToLogin,
        onPasswordResetSent = forgotPasswordViewModel::resetPassword
    )
}