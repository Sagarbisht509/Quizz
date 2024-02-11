package com.sagar.quizz.data.model.remote.auth

data class UserRequest(
    val avatar: String,
    val email: String,
    val password: String,
    val username: String
)