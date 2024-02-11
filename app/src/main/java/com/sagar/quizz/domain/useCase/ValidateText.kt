package com.sagar.quizz.domain.useCase

class ValidateText {
    fun execute(input: String): ValidationResult {
        if (input.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Field can't be empty"
            )
        }
        if (input.contains("[0-9]".toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = "digits are not allowed"
            )
        }
        if (input.contains("[!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~]".toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = "Special characters are not allowed"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}