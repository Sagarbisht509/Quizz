package com.sagar.quizz.data.model.remote.user

data class UpdateUserDetailRequest(
    val userId: String,
    val avatar: String? = null,
    val exp: Int? = null,
    val quizAttempt: Int? = null
)
