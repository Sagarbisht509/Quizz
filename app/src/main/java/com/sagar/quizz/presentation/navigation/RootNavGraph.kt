package com.sagar.quizz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sagar.quizz.QuizzApp
import com.sagar.quizz.presentation.onBoarding.OnBoardingScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(route = Destination.OnBoarding.route) {
            OnBoardingScreen(
                onNavigateToAuth = { navController.navigateToAuth() }
            )
        }

        authNavGraph(navController = navController)

        composable(route = Destination.Main.route) {
            QuizzApp()
        }
    }
}

fun NavController.navigateToAuth() = navigate(route = Destination.Auth.route) {
    popUpTo(route = Destination.OnBoarding.route) {
        inclusive = true
    }
}