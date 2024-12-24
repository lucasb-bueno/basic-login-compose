package com.lucasbueno.basiclogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasbueno.basiclogin.presentation.login.SignInRoute
import com.lucasbueno.basiclogin.presentation.login.signInRoute
import com.lucasbueno.basiclogin.presentation.profile.ProfileScreenRoute
import com.lucasbueno.basiclogin.presentation.profile.profileScreenRoute
import com.lucasbueno.basiclogin.presentation.signup.SignUpRoute
import com.lucasbueno.basiclogin.presentation.signup.signUpRoute
import com.lucasbueno.basiclogin.ui.theme.BasicLoginTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicLoginActivity : ComponentActivity() {

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
                    NavHost(navController = navController, startDestination = signInRoute) {
                        composable(signInRoute) {
                            SignInRoute(
                                onNavigateToProfileScreen = {
                                    navController.navigate(profileScreenRoute)
                                },
                                onNavigateToSignUpScreen = {
                                    navController.navigate(signUpRoute)
                                }
                            )
                        }
                        composable(profileScreenRoute) {
                            ProfileScreenRoute(
                                onLogoutSuccess = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(signUpRoute) {
                            SignUpRoute(
                                onSignUpSuccess = { navController.navigate(profileScreenRoute) },
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}