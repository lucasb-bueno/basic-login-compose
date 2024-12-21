package com.lucasbueno.basiclogin.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucasbueno.basiclogin.presentation.signin.GoogleAuthUiClient

const val profileScreenRoute = "profileScreenRoute"

@Composable
fun ProfileScreenRoute(
    googleAuthUiClient: GoogleAuthUiClient,
    onLogoutSuccess: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.onProfileResult(googleAuthUiClient.getSignedInUser())

    ProfileScreen(
        profileState = state,
        onSignOutClick = { viewModel.logout(googleAuthUiClient) },
        onLogoutSuccess = {
            onLogoutSuccess()
        }
    )
}