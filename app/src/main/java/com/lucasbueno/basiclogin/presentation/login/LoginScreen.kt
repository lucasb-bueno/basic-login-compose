import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.component.DefaultButton
import com.lucasbueno.basiclogin.component.PasswordTextField
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.presentation.login.LogInState

@Composable
fun LoginScreen(
    state: DataState<LogInState>,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithEmailAndPasswordClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordButtonClick: () -> Unit,
    onSuccessLogin: () -> Unit,
) {
    LaunchedEffect(state) {
        if (state is DataState.Success && state.data?.isLoginSuccess == true) {
            onSuccessLogin()
        }
    }

    ScreenContent(
        uiState = state,
        onLoginWithGoogleClick = onLoginWithGoogleClick,
        onLoginWithEmailAndPasswordClick = onLoginWithEmailAndPasswordClick,
        onCreateAccountClick = onCreateAccountClick,
        onForgotPasswordButtonClick = onForgotPasswordButtonClick
    )
}

@Composable
fun ScreenContent(
    uiState: DataState<LogInState>,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithEmailAndPasswordClick: (String, String) -> Unit,
    onForgotPasswordButtonClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val isEmailSignInLoading =
        uiState is DataState.Success && uiState.data?.emailSignInLoading == true
    val isGoogleSignInLoading =
        uiState is DataState.Success && uiState.data?.googleSignInLoading == true

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to the Login App",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = context.getString(R.string.common_email)) },
                modifier = Modifier.fillMaxWidth()
            )

            PasswordTextField(
                modifier = Modifier.padding(bottom = 4.dp),
                password = password,
                onTextChange = { password = it })

            if (uiState is DataState.Error) {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = isEmailSignInLoading,
                text = context.getString(R.string.sign_in_with_email_button_label),
                onClick = {
                    onLoginWithEmailAndPasswordClick(email, password)
                    email = ""
                    password = ""
                    focusManager.clearFocus()
                },
                icon = Icons.Default.Email
            )

            DefaultButton(
                text = context.getString(R.string.create_an_account_button_label),
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = context.getString(R.string.common_or_label),
                style = MaterialTheme.typography.bodyLarge
            )

            DefaultButton(
                isLoading = isGoogleSignInLoading,
                text = context.getString(R.string.sign_in_with_google_button_label),
                onClick = onLoginWithGoogleClick,
                modifier = Modifier
                    .fillMaxWidth(),
                icon = Icons.Default.Android
            )

            TextButton(onClick = onForgotPasswordButtonClick) {
                Text(
                    text = "Forgot Password?",
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        state = DataState.Success(
            LogInState(
                isLoginSuccess = false,
                emailSignInLoading = false,
                googleSignInLoading = false
            )
        ),
        onLoginWithGoogleClick = {},
        onLoginWithEmailAndPasswordClick = { _, _ -> },
        onCreateAccountClick = {},
        onForgotPasswordButtonClick = {},
        onSuccessLogin = {}
    )
}
