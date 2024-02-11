package com.sagar.quizz.data.model.remote.auth

data class User(
    val __v: Int,
    val _id: String,
    val avatar: String,
    val createdAt: String,
    val email: String,
    val exp: Int,
    val password: String,
    val updatedAt: String,
    val username: String,
    val verified: Boolean,
    val favoriteQuizIds: List<String> = emptyList(),
    val quizAttempt: Int
)