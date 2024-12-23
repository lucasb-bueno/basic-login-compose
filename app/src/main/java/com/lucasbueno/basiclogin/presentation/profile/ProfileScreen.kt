package com.lucasbueno.basiclogin.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucasbueno.basiclogin.component.DefaultButton
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.domain.model.UserData

@Composable
fun ProfileScreen(
    profileState: DataState<ProfileState>,
    onSignOutClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onRetryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    LaunchedEffect(key1 = profileState) {
        if (profileState is DataState.Success && profileState.data?.shouldLogOut == true) {
            onLogoutSuccess()
        }
    }

    when (profileState) {
        is DataState.Success -> {
            profileState.data?.userData?.let {
                Content(userData = it, onSignOut = onSignOutClick)
            }
        }

        is DataState.Error -> ErrorScreen(
            message = profileState.message,
            onRetryClick = onRetryClick,
            onLogoutClick = onLogoutClick
        )

        is DataState.Loading -> LoadingScreen()
        is DataState.Default -> Unit
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(message: String, onRetryClick: () -> Unit, onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)

        DefaultButton(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Retry",
            onClick = onRetryClick
        )

        DefaultButton(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Logout",
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun Content(userData: UserData, onSignOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userData.userId, modifier = Modifier.padding(bottom = 12.dp))

        if (userData.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData.userName != null) {
            Text(
                text = userData.userName,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}