package com.sagar.quizz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sagar.quizz.presentation.quiz.quizResult.QuizResultScreen
import com.sagar.quizz.presentation.quiz.playQuiz.PlayQuizViewModel
import com.sagar.quizz.presentation.quiz.playQuiz.QuizScreen
import com.sagar.quizz.presentation.quiz.quizDetails.QuizDetailsScreen

fun NavGraphBuilder.quizNavGraph(navController: NavHostController) {
    navigation(
        route = Destination.Quiz.route,
        startDestination = Destination.Quiz.QuizDetails.route
    ) {

        composable(
            route = Destination.Quiz.QuizDetails.route.plus("/{quizId}"),
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) {
            val viewModel = it.sharedViewModel<PlayQuizViewModel>(navController)
            val quizId = it.arguments!!.getString("quizId")
            QuizDetailsScreen(
                quizId = quizId!!,
                viewModel = viewModel,
                onNavigateToPlayQuizScreen = {
                    navController.navigateToPlayQuizScreen()
                    viewModel.startQuiz()
                }
            )
        }

        composable(route = Destination.Quiz.PlayQuiz.route) {
            val viewModel = it.sharedViewModel<PlayQuizViewModel>(navController)
            QuizScreen(
                viewModel = viewModel,
                onNavigateToResultScreen = { navController.navigateToQuizResultScreen() },
                onNavigateToHomeScreen = { navController.navigateToHomeScreen() }
            )
        }

        composable(route = Destination.Quiz.QuizResult.route) {
            val viewModel = it.sharedViewModel<PlayQuizViewModel>(navController)
            QuizResultScreen(
                viewModel = hiltViewModel(),
                onNavigateToHomeScreen = { navController.navigateToHomeScreen() },
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun NavController.navigateToHomeScreen() = navigate(route = Destination.Main.route) {
    popUpTo(route = Destination.Quiz.route) {
        inclusive = true
    }
}

fun NavController.navigateToPlayQuizScreen() = navigate(route = Destination.Quiz.PlayQuiz.route)

fun NavController.navigateToQuizResultScreen() = navigate(route = Destination.Quiz.QuizResult.route)