package com.sagar.quizz.presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sagar.quizz.presentation.main.favourite.FavouriteScreen
import com.sagar.quizz.presentation.main.home.HomeScreen
import com.sagar.quizz.presentation.main.profile.ProfileScreen
import com.sagar.quizz.presentation.main.search.SearchScreen
import com.sagar.quizz.presentation.quiz.createQuiz.CreateQuizScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Destination.Main.route,
        startDestination = Destination.Main.Home.route
    ) {
        composable(route = Destination.Main.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                onNavigateToCreateQuizScreen = { navController.navigate(route = Destination.CreateQuiz.route) },
                onNavigateToQuizDetailScreen = { quizId -> navController.navigateToQuizDetail(quizId = quizId) }
            )
        }

        composable(route = Destination.Main.Search.route) {
            SearchScreen(
                viewModel = hiltViewModel(),
                onNavigateToQuizDetailScreen = { quizId -> navController.navigateToQuizDetail(quizId = quizId) }
            )
        }

        composable(route = Destination.Main.Favourite.route) {
            FavouriteScreen(
                viewModel = hiltViewModel(),
                onNavigateToQuizDetailScreen = { quizId -> navController.navigateToQuizDetail(quizId = quizId) }
            )
        }

        composable(
            route = Destination.Main.Profile.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 500)
                )
            }
        ) {
            ProfileScreen(
                viewModel = hiltViewModel(),
                onNavigateToAuthScreen = { navController.navigateToAuthRoute() }
            )
        }

        composable(route = Destination.CreateQuiz.route) {
            CreateQuizScreen(
                viewModel = hiltViewModel(),
                onNavigateToHomeScreen = { navController.popBackStack() }
            )
        }

        quizNavGraph(navController = navController)
    }
}

fun NavController.navigateToAuthRoute() = navigate(route = Destination.Auth.route) {
    popUpTo(route = Destination.Main.route) {
        inclusive = true
    }
}

fun NavController.navigateToQuizDetail(quizId: String) = navigate(route = Destination.Quiz.QuizDetails.route.plus("/${quizId}"))