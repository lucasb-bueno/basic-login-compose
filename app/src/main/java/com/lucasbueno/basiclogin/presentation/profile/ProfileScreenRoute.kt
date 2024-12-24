package com.lucasbueno.basiclogin.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

const val profileScreenRoute = "profileScreenRoute"

@Composable
fun ProfileScreenRoute(
    onLogoutSuccess: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    ProfileScreen(
        profileState = profileState,
        onSignOutClick = viewModel::logout,
        onLogoutSuccess = {
            onLogoutSuccess()
            viewModel.onLogoutDone()
        },
        onRetryClick = viewModel::fetchData,
        onLogoutClick = viewModel::logout
    )
}