package com.sagar.quizz.presentation.main.home

sealed class HomeScreenEvent() {
    data class QuizCodeChanged(val quizCode: String): HomeScreenEvent()
    data class QuizCodeStateChanged(val value: Boolean): HomeScreenEvent()

    object JoinQuizButtonClicked: HomeScreenEvent()
}
