import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.presentation.signin.LogInState

@Composable
fun SignInScreen(
    state: DataState<LogInState>,
    onSignInWithGoogleClick: () -> Unit,
    onSignInWithEmailAndPasswordClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state) {
        if (state is DataState.Error) {
            Toast.makeText(
                context,
                state.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    ScreenContent(
        onSignInWithGoogleClick = onSignInWithGoogleClick,
        onSignInWithEmailAndPasswordClick = onSignInWithEmailAndPasswordClick,
        onCreateAccountClick = onCreateAccountClick
    )
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "An error occurred. Please try again later.")
    }
}

@Composable
fun ScreenContent(
    onSignInWithGoogleClick: () -> Unit,
    onSignInWithEmailAndPasswordClick: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

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

            Button(
                onClick = { onSignInWithEmailAndPasswordClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign in with Email")
            }

            Button(
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Create an Account")
            }

            Text(text = "OR", style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = onSignInWithGoogleClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign in with Google")
            }
        }
    }
}
