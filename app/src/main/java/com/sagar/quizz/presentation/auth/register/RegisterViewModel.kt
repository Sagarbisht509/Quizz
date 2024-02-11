package com.sagar.quizz.presentation.auth.register

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.repo.auth.AuthRepository
import com.sagar.quizz.domain.useCase.ValidateEmail
import com.sagar.quizz.domain.useCase.ValidatePassword
import com.sagar.quizz.utill.Constants.OTP_TIME
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()

    private val _uiState = MutableStateFlow(RegisterState())
    var uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) = viewModelScope.launch {
        when (event) {

            is RegisterEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
                if (uiState.value.name.isNotEmpty()) {
                    _uiState.update { it.copy(nameError = null) }
                }
            }

            is RegisterEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
                validateEmail()
            }

            is RegisterEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
                validatePassword()
            }

            is RegisterEvent.AvatarChanged -> {
                _uiState.update {
                    it.copy(
                        selectedAvatar = uiState.value.avatars[event.index],
                        selectedAvatarIndex = event.index
                    )
                }
            }

            is RegisterEvent.PasswordVisibilityChanged -> {
                _uiState.update { it.copy(isPasswordVisible = event.isPasswordVisible) }
            }

            is RegisterEvent.NextButtonClicked -> {
                _uiState.update { it.copy(showAvatarContent = true) }
            }

            is RegisterEvent.RegisterButtonClicked -> {
                registerUser(
                    AuthRequest(
                        username = _uiState.value.name,
                        email = _uiState.value.email,
                        password = _uiState.value.password,
                        avatar = _uiState.value.selectedAvatar
                    )
                )
            }
        }
    }

    private fun validateName(): Boolean {
        if (_uiState.value.email.isEmpty()) {
            _uiState.update { it.copy(nameError = "Name can't be empty") }
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val emailResult = validateEmail.execute(_uiState.value.email)
        _uiState.update { it.copy(emailError = emailResult.errorMessage) }
        return emailResult.successful
    }

    private fun validatePassword(): Boolean {
        val passwordResult = validatePassword.execute(_uiState.value.password)
        _uiState.update { it.copy(passwordError = passwordResult.errorMessage) }
        return passwordResult.successful
    }

    private suspend fun registerUser(authRequest: AuthRequest) {
        authRepo.registerUser(authRequest).collectLatest {
            when (it) {
                is Resource.Error -> _uiState.update { updatedState ->
                    updatedState.copy(toastMessage = it.message)
                }

                is Resource.Loading -> _uiState.update { updatedState ->
                    updatedState.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _uiState.update { updatedState ->
                        updatedState.copy(successfullyRegistered = true, isLoading = false)
                    }
                }
            }
        }
    }

    // ********* Not Implemented Yet ***************

    private val timer = object : CountDownTimer(OTP_TIME, 1000L) {
        override fun onTick(p0: Long) {
            if (p0 > 0) {
                _uiState.update { it.copy(timeLeft = p0) }
            }
        }

        override fun onFinish() {
            _uiState.update { it.copy(timeLeft = 0L) }
        }
    }

    private fun restartTimer() = viewModelScope.launch {
        _uiState.update { it.copy(timeLeft = 0L) }
        timer.cancel()
        timer.start()
    }

    private suspend fun resendOtp() {
        authRepo.resendOTP(userId = "", type = "VERIFICATION").collectLatest {
            when (it) {
                is Resource.Loading -> Unit
                is Resource.Success -> Unit
                is Resource.Error -> Unit
            }
        }
    }

    private suspend fun verifyOTP(userId: String, otp: Int, type: String) {
        authRepo.verifyOTP(userId, otp, type).collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> Unit
                is Resource.Success -> Unit
            }
        }
    }
}