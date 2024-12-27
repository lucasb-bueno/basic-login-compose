package com.lucasbueno.basiclogin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasbueno.basiclogin.presentation.forgotpassword.ForgotPasswordRoute
import com.lucasbueno.basiclogin.presentation.forgotpassword.forgotPasswordRoute
import com.lucasbueno.basiclogin.presentation.login.SignInRoute
import com.lucasbueno.basiclogin.presentation.login.loginRoute
import com.lucasbueno.basiclogin.presentation.profile.ProfileScreenRoute
import com.lucasbueno.basiclogin.presentation.profile.profileScreenRoute
import com.lucasbueno.basiclogin.presentation.signup.SignUpRoute
import com.lucasbueno.basiclogin.presentation.signup.signUpRoute

@Composable
fun BasicLoginNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = loginRoute) {
        composable(loginRoute) {
            SignInRoute(
                onNavigateToProfileScreen = {
                    navController.navigate(profileScreenRoute)
                },
                onNavigateToSignUpScreen = {
                    navController.navigate(signUpRoute)
                },
                onNavigateToForgotPasswordScreen = {
                    navController.navigate(forgotPasswordRoute)
                }
            )
        }
        composable(profileScreenRoute) {
            ProfileScreenRoute(
                onLogoutSuccess = {
                    navController.popBackStack(
                        route = loginRoute,
                        inclusive = false
                    )
                }
            )
        }
        composable(signUpRoute) {
            SignUpRoute(
                onSignUpSuccess = { navController.navigate(profileScreenRoute) },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(forgotPasswordRoute) {
            ForgotPasswordRoute(
                navigateToLogin = { navController.navigateUp() }
            )
        }
    }
}