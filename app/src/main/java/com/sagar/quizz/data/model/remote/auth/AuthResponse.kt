package com.sagar.quizz.data.model.remote.auth

data class AuthResponse(
    val user: User,
    val token: String
)