package com.sagar.quizz.domain.useCase

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }
        if (!isPasswordValid(password)) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist at least one digit and one letter"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.any { it.isDigit() } &&
                password.any { it.isLetter() }
    }
}