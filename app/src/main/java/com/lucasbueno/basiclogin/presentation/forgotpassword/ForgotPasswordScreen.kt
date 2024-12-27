package com.lucasbueno.basiclogin.presentation.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.component.DefaultButton
import com.lucasbueno.basiclogin.component.LoginTopBar
import com.lucasbueno.basiclogin.core.DataState

@Composable
fun ForgotPasswordScreen(
    state: DataState<Unit>,
    onPasswordResetSent: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            LoginTopBar(onBackClick = onBackClick)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            DefaultButton(
                isLoading = state is DataState.Loading,
                onClick = {
                    onPasswordResetSent(email)
                },
                text = "Send Reset Link",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            if (state is DataState.Success) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Email sent successfully.",
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (state is DataState.Error) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        state = DataState.Success(Unit),
        onPasswordResetSent = {},
        onBackClick = {}
    )
}
