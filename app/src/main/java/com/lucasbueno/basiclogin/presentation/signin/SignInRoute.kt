package com.lucasbueno.basiclogin.presentation.signin

import SignInScreen
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lucasbueno.basiclogin.domain.AuthProvider
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.presentation.profile.profileScreenRoute
import com.lucasbueno.basiclogin.presentation.signup.signUpRoute
import kotlinx.coroutines.launch

const val signInRoute = "signInRoute"

@Composable
fun SignInRoute(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    authProvider: AuthProvider,
) {
    val viewModel: SignInViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val logInState = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(logInState)
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (googleAuthUiClient.getSignedInUser() is DataState.Success) {
            navController.navigate(profileScreenRoute)
        }
    }

    LaunchedEffect(state) {
        if (state is DataState.Success) {
            navController.navigate(profileScreenRoute)
            viewModel.resetState()
        }
    }

    SignInScreen(
        state = state,
        onSignInWithGoogleClick = {
            coroutineScope.launch {
                val signInIntentSender = googleAuthUiClient.loginWithGoogle()
                launcher.launch(
                    IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
                )
            }
        },
        onSignInWithEmailAndPasswordClick = { email, password ->
            coroutineScope.launch {
                val result = authProvider.login(email, password)
                result?.let { viewModel.onSignInResult(it) }
            }
        },
        onCreateAccountClick = {
            navController.navigate(signUpRoute)
        }
    )
}