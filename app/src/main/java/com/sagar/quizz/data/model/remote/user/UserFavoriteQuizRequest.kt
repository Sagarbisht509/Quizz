package com.sagar.quizz.data.model.remote.user

data class UserFavoriteQuizRequest(
    val userId: String,
    val favoriteQuizIds: List<String>
)