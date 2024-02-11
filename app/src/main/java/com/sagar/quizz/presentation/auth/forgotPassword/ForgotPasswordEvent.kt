package com.sagar.quizz.presentation.auth.forgotPassword


sealed class ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent()
    data class OtpChanged(val otp: String) : ForgotPasswordEvent()
    data class PasswordChanged(val password: String) : ForgotPasswordEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : ForgotPasswordEvent()
    data class PasswordVisibilityChanged(val isPasswordVisible: Boolean) : ForgotPasswordEvent()
    data class VisibilityChanged(val visible: Boolean) : ForgotPasswordEvent()

    object Submit : ForgotPasswordEvent()
    object ResendOTP : ForgotPasswordEvent()
}
