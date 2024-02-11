package com.sagar.quizz.data.model.remote.quiz

data class Question(
    val question: String = "",
    val options: List<String> = emptyList(),
    val answer: Int = 5,
)
