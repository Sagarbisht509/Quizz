package com.sagar.quizz.presentation.auth.login

sealed class LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class PasswordVisibilityChanged(val isPasswordVisible: Boolean) : LoginEvent()

    object LoginButtonClicked : LoginEvent()
}
