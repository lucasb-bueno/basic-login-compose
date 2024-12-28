package com.lucasbueno.basiclogin.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.components.DefaultButton
import com.lucasbueno.basiclogin.components.DefaultLoadingScreen
import com.lucasbueno.basiclogin.components.ErrorContainer
import com.lucasbueno.basiclogin.components.LoginTopBar
import com.lucasbueno.basiclogin.components.PasswordTextField
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
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    var showError by remember { mutableStateOf(signUpState is DataState.Error) }

    val model = SignUpModel(
        email = email.trim(),
        password = password.trim(),
        userName = username.trim()
    )

    LaunchedEffect(signUpState) {
        showError = signUpState is DataState.Error
    }

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
                label = { Text(context.getString(R.string.common_email)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            PasswordTextField(
                modifier = Modifier.padding(bottom = 30.dp),
                password = password,
                onTextChange = { password = it },
                keyboardController = keyboardController,
                onImeAction = { onSignUpClick(model) }
            )

            DefaultButton(
                text = context.getString(R.string.create_account_button_label),
                onClick = {
                    showError = signUpState is DataState.Error
                    onSignUpClick(model)
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (signUpState is DataState.Error && showError) {
                ErrorContainer(
                    message = signUpState.message,
                    onDismiss = {
                        showError = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    SignUpScreen(
        signUpState = DataState.Success(Unit),
        onBackClick = {},
        onSignUpClick = { _ -> },
        onSuccessRegister = {}
    )
}
