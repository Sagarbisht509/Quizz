package com.sagar.quizz.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sagar.quizz.presentation.auth.login.LoginScreen
import com.sagar.quizz.presentation.auth.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Destination.Auth.route,
        startDestination = Destination.Auth.Login.route
    ) {

        composable(route = Destination.Auth.Login.route) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onNavigateToSignup = { navController.navigateToSignUp() },
                onNavigateToHome = { navController.navigateToHome() },
                onNavigateToForgotPasswordScreen = {}
            )
        }

        composable(route = Destination.Auth.Register.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                onNavigateToSignIn = { navController.navigateToSignIn() },
                onNavigateToHome = { navController.navigateToHome() }
            )
        }

    }
}

fun NavController.navigateToSignIn() = navigate(route = Destination.Auth.Login.route) {
    popUpTo(route = Destination.Auth.Register.route) {
        inclusive = true
    }
}

fun NavController.navigateToSignUp() = navigate(route = Destination.Auth.Register.route)

fun NavController.navigateToHome() = navigate(route = Destination.Main.route) {
    popUpTo(route = Destination.Auth.route) {
        inclusive = true
    }
}
