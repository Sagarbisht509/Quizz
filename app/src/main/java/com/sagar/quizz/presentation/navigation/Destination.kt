package com.sagar.quizz.presentation.navigation

sealed class Destination(
    val route: String
) {
    object OnBoarding : Destination(route = "OnBoarding")

    object Auth : Destination(route = "auth") {
        object Login : Destination(route = "Login")
        object Register : Destination(route = "Register")
        object ForgotPassword : Destination(route = "ForgotPassword")
    }

    object Main : Destination(route = "main") {
        object Home : Destination(route = "Home")
        object Search : Destination(route = "Search")
        object Favourite : Destination(route = "Favourite")
        object Profile : Destination(route = "Profile")
    }

    object Quiz : Destination(route = "quiz") {
        object QuizDetails : Destination(route = "QuizDetails")
        object PlayQuiz : Destination(route = "PlayQuiz")
        object QuizResult : Destination(route = "QuizResult")
    }

    object CreateQuiz : Destination(route = "CreateQuiz")
}
