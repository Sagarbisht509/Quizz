package com.sagar.quizz.domain.useCase

class ValidateOtp {
    fun execute(otp: String): ValidationResult {
        if (otp.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "OTP can't be blank"
            )
        }

        if (!isDigits(otp)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please enter a valid OTP with exactly four digits"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun isDigits(otp: String): Boolean {
        val regex = Regex("\\d{4}")
        return regex.matches(otp)
    }
}
