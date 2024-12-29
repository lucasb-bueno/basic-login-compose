import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.R
import com.lucasbueno.basiclogin.components.DefaultButton
import com.lucasbueno.basiclogin.components.ErrorContainer
import com.lucasbueno.basiclogin.components.PasswordTextField
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
    ScreenContent(
        uiState = state,
        onLoginWithGoogleClick = onLoginWithGoogleClick,
        onLoginWithEmailAndPasswordClick = onLoginWithEmailAndPasswordClick,
        onCreateAccountClick = onCreateAccountClick,
        onForgotPasswordButtonClick = onForgotPasswordButtonClick,
        onSuccessLogin = onSuccessLogin
    )
}

@Composable
fun ScreenContent(
    uiState: DataState<LogInState>,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithEmailAndPasswordClick: (String, String) -> Unit,
    onForgotPasswordButtonClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onSuccessLogin: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isEmailSignInLoading =
        uiState is DataState.Success && uiState.data?.emailSignInLoading == true
    val isGoogleSignInLoading =
        uiState is DataState.Success && uiState.data?.googleSignInLoading == true
    var showError by remember { mutableStateOf(false) }


    LaunchedEffect(uiState) {
        if (uiState is DataState.Success && uiState.data?.isLoginSuccess == true) {
            onSuccessLogin()
            focusManager.clearFocus()
            email = ""
            password = ""
            onSuccessLogin()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), // Light gradient start
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) // Light gradient end
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_app_logo),
                contentDescription = "Login App Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = context.getString(R.string.common_email)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            PasswordTextField(
                modifier = Modifier.padding(bottom = 4.dp),
                password = password,
                onTextChange = { password = it },
                keyboardController = keyboardController,
                onImeAction = {
                    showError = uiState is DataState.Error
                    onLoginWithEmailAndPasswordClick(email, password)
                }
            )

            TextButton(
                modifier = Modifier.align(alignment = Alignment.End),
                onClick = onForgotPasswordButtonClick
            ) {
                Text(
                    text = context.getString(R.string.forgot_password_label),
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            if (showError && uiState is DataState.Error) {
                ErrorContainer(
                    message = uiState.message,
                    onDismiss = { showError = false }
                )
            }

            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                isLoading = isEmailSignInLoading,
                text = context.getString(R.string.sign_in_with_email_button_label),
                onClick = {
                    showError = uiState is DataState.Error
                    onLoginWithEmailAndPasswordClick(email, password)
                },
                icon = Icons.Default.Email
            )

            DefaultButton(
                text = context.getString(R.string.create_an_account_button_label),
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                modifier = Modifier.padding(vertical = 4.dp),
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
        }
    }
}

class LoginScreenPreviewProvider : PreviewParameterProvider<DataState<LogInState>> {
    override val values = sequenceOf(
        DataState.Success(
            LogInState(
                isLoginSuccess = false,
                emailSignInLoading = true,
                googleSignInLoading = false
            )
        ),
        DataState.Error(message = "Invalid credentials")
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewProvider::class) state: DataState<LogInState>
) {
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
