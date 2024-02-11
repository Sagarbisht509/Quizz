package com.sagar.quizz.presentation.main.favourite

import com.sagar.quizz.data.model.remote.quiz.Quiz

data class FavouritesScreenState(
    val quizzes: List<Quiz> = emptyList(),
    val isLoading: Boolean = false,
    val quizId: String? = null,
    val message: String? = null
) {
    val isEmptyStateVisible: Boolean
        get() = quizzes.isEmpty() && !isLoading
}