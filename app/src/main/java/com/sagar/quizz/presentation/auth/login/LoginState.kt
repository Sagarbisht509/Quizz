package com.sagar.quizz.presentation.auth.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val successfullyLoggedIn: Boolean = false,
    val toastMessage: String? = null
)