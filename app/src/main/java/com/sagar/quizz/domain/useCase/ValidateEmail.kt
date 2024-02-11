package com.sagar.quizz.domain.useCase

import android.util.Patterns

class ValidateEmail {
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email can't be blank"
            )
        }
        if (!isEmailValid(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please enter valid email"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}