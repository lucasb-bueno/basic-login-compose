package com.lucasbueno.basiclogin.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.component.DefaultButton
import com.lucasbueno.basiclogin.component.DefaultLoadingScreen
import com.lucasbueno.basiclogin.component.LoginTopBar
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.domain.model.SignUpModel

@Composable
fun SignUpScreen(
    signUpState: DataState<Unit>,
    modifier: Modifier = Modifier,
    onSignUpClick: (SignUpModel) -> Unit,
    onSuccessRegister: () -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(key1 = signUpState) {
        if (signUpState is DataState.Success) {
            onSuccessRegister()
        }
    }

    when (signUpState) {
        DataState.Loading -> DefaultLoadingScreen(optionalText = "Creating account...")
        else -> SignUpContent(
            signUpState = signUpState,
            modifier = modifier,
            onSignUpClick = onSignUpClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun SignUpContent(
    signUpState: DataState<Unit>,
    modifier: Modifier = Modifier,
    onSignUpClick: (SignUpModel) -> Unit,
    onBackClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            LoginTopBar(onBackClick = onBackClick)
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                text = if (username.isNotEmpty()) "Welcome, $username!" else ""
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Choose your Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            if (signUpState is DataState.Error && signUpState.message.contains(
                    "email",
                    ignoreCase = true
                )
            ) {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = signUpState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Choose your Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            if (signUpState is DataState.Error && signUpState.message.contains(
                    "password",
                    ignoreCase = true
                )
            ) {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = signUpState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            DefaultButton(
                text = "Create Account",
                onClick = {
                    onSignUpClick(
                        SignUpModel(
                            email = email.trim(),
                            password = password.trim(),
                            userName = username.trim()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (signUpState is DataState.Error && !signUpState.message.contains(
                    "email",
                    ignoreCase = true
                ) && !signUpState.message.contains("password", ignoreCase = true)
            ) {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = signUpState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
