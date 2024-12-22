package com.lucasbueno.basiclogin.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasbueno.basiclogin.presentation.login.GoogleAuthUiClient

const val profileScreenRoute = "profileScreenRoute"

@Composable
fun ProfileScreenRoute(
    googleAuthUiClient: GoogleAuthUiClient,
    onLogoutSuccess: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    viewModel.onProfileResult(googleAuthUiClient.getSignedInUser())

    ProfileScreen(
        profileState = profileState,
        onSignOutClick = { viewModel.logout(googleAuthUiClient) },
        onLogoutSuccess = {
            onLogoutSuccess()
        }
    )
}