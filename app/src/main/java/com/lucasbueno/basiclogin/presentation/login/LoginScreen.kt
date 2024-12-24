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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.component.DefaultButton
import com.lucasbueno.basiclogin.core.DataState
import com.lucasbueno.basiclogin.presentation.login.LogInState

@Composable
fun LoginScreen(
    state: DataState<LogInState>,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithEmailAndPasswordClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit,
    onSuccessLogin: () -> Unit
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
        onCreateAccountClick = onCreateAccountClick
    )
}

@Composable
fun ScreenContent(
    uiState: DataState<LogInState>,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithEmailAndPasswordClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit
) {
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

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
                text = "Sign in with Email",
                onClick = {
                    onLoginWithEmailAndPasswordClick(email, password)
                    email = ""
                    password = ""
                    focusManager.clearFocus()
                },
                icon = Icons.Default.Email
            )

            DefaultButton(
                text = "Create an Account",
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "OR", style = MaterialTheme.typography.bodyLarge)

            DefaultButton(
                isLoading = isGoogleSignInLoading,
                text = "Sign in with Google",
                onClick = onLoginWithGoogleClick,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Android
            )
        }
    }
}
