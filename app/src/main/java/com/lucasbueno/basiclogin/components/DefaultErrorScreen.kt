package com.lucasbueno.basiclogin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultErrorScreen(message: String, onRetryClick: () -> Unit, onLogoutClick: () -> Unit) {
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