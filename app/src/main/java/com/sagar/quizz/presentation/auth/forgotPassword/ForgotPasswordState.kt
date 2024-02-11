package com.sagar.quizz.presentation.auth.forgotPassword

data class ForgotPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val otp: String = "",
    val otpError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val submitButtonText: String = "Get OTP",
    val timeLeft: Long = 0L,
    val isOtpViewVisible: Boolean = false,
    val isOtpVerified: Boolean = false,
    val toastMessage: String? = null
) {
    val otpTimeRemaining: String
        get() {
            val totalSeconds = timeLeft / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return if (minutes == 0L) "$seconds" else "$minutes:$seconds"
        }
}
