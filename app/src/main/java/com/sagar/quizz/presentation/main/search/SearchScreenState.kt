package com.sagar.quizz.presentation.main.search

import com.sagar.quizz.data.model.remote.quiz.Category
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.utill.Constants

data class SearchScreenState(
    val categories: List<Category> = Constants.categoryList,
    val isSearching: Boolean = false,
    val isLoading: Boolean = false
)
