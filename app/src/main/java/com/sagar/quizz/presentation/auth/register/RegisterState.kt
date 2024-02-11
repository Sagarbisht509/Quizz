package com.sagar.quizz.presentation.auth.register

import com.sagar.quizz.utill.Constants

data class RegisterState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val timeLeft: Long = 0L,
    val isPasswordVisible: Boolean = false,
    val showAvatarContent: Boolean = false,
    val avatars: List<String> = Constants.avatars,
    val selectedAvatarIndex: Int = 4,
    val selectedAvatar: String = "",
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val successfullyRegistered: Boolean = false
) {

    val isNextButtonVisible: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && emailError == null &&
                password.isNotBlank() && passwordError == null

    val otpTimeRemaining: String
        get() {
            val totalSeconds = timeLeft / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return if (minutes == 0L) "$seconds" else "$minutes:$seconds"
        }
}
