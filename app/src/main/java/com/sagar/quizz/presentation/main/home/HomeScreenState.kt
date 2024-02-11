package com.sagar.quizz.presentation.main.home

import com.sagar.quizz.data.model.remote.quiz.Quiz

data class HomeScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val quizCode: String = "",
    val openAlertDialog: Boolean = false,
    val topRatedQuiz: List<Quiz> = emptyList(),

    val quizId: String = "",
    val errorMessage: String? = null
) {
    val showConformationButton: Boolean
        get() = quizCode.length == 4

    val quizCodeError: Boolean
        get() = quizCode.isNotBlank() && quizCode.length != 4
}
