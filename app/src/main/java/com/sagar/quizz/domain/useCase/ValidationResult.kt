package com.sagar.quizz.domain.useCase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
