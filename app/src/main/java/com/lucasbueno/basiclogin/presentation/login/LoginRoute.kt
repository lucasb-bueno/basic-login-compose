package com.lucasbueno.basiclogin.presentation.login

import LoginScreen
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

const val loginRoute = "loginRoute"

@Composable
fun SignInRoute(
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToSignUpScreen: () -> Unit,
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.onGoogleLoginResult(result.data)
            }
        }
    )

    LaunchedEffect(viewModel.uiEvents) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is LoginUiEvent.GoogleSignIn -> launcher.launch(
                    IntentSenderRequest.Builder(event.intentSender).build()
                )
            }
        }
    }

    LoginScreen(
        state = state,
        onLoginWithGoogleClick = { viewModel.handleGoogleLogin() },
        onLoginWithEmailAndPasswordClick = { email, password ->
            viewModel.loginWithEmailAndPassword(email, password)
        },
        onCreateAccountClick = onNavigateToSignUpScreen,
        onSuccessLogin = {
            onNavigateToProfileScreen()
            viewModel.resetLoginState()
        }
    )
}