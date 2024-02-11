package com.sagar.quizz.presentation.main.favourite

import com.sagar.quizz.data.model.remote.quiz.Quiz

sealed class FavouritesScreenEvents {
    data class ShowToast(val message: String) : FavouritesScreenEvents()
    data class NavigateToQuizDetailScreen(val quiz: Quiz) : FavouritesScreenEvents()
}