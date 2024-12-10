package com.lucasbueno.basiclogin

import SignInScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.lucasbueno.basiclogin.domain.DataState
import com.lucasbueno.basiclogin.presentation.profile.ProfileScreen
import com.lucasbueno.basiclogin.presentation.signin.GoogleAuthUiClient
import com.lucasbueno.basiclogin.presentation.signin.SignInViewModel
import com.lucasbueno.basiclogin.presentation.signup.FirebaseAuthClient
import com.lucasbueno.basiclogin.presentation.signup.SignUpRoute
import com.lucasbueno.basiclogin.ui.theme.BasicLoginTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            googleSignInClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val authProvider by lazy {
        FirebaseAuthClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BasicLoginTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("profile")
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val logInState = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(logInState)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state) {
                                if (state is DataState.Success) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("profile")
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInWithGoogleClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender =
                                            googleAuthUiClient.loginWithGoogle()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                onSignInWithEmailAndPasswordClick = { email, password ->
                                    lifecycleScope.launch {
                                        val result = authProvider.login(
                                            email,
                                            password
                                        )
                                        result?.let {
                                            viewModel.onSignInResult(it)
                                        }
                                    }
                                },
                                onCreateAccountClick = {
                                    navController.navigate("signup")
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.logout()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("sign_in")
                                    }
                                }
                            )
                        }
                        composable("signup") {
                            SignUpRoute(
                                onSignUpClick = { email, password ->
                                    lifecycleScope.launch {
                                        authProvider.registerUser(
                                            email = email,
                                            password = password,
                                            onResult = { isSuccess, errorMessage ->
                                                if (isSuccess) {
                                                    navController.navigate("profile")
                                                } else {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        errorMessage,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        )
                                    }
                                },
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}