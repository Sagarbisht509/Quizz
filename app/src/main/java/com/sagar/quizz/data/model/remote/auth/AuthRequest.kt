package com.sagar.quizz.data.model.remote.auth

data class AuthRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val avatar: String? = null
)
