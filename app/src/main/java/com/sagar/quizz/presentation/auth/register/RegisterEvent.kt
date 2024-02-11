package com.sagar.quizz.presentation.auth.register

sealed class RegisterEvent {

    data class NameChanged(val name: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class PasswordVisibilityChanged(val isPasswordVisible: Boolean) : RegisterEvent()
    data class AvatarChanged(val index: Int) : RegisterEvent()

    object NextButtonClicked: RegisterEvent()
    object RegisterButtonClicked: RegisterEvent()
}
