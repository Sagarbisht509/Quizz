package com.sagar.quizz.presentation.auth.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.repo.auth.AuthRepository
import com.sagar.quizz.domain.useCase.ValidateEmail
import com.sagar.quizz.domain.useCase.ValidateOtp
import com.sagar.quizz.domain.useCase.ValidatePassword
import com.sagar.quizz.domain.useCase.ValidateRepeatedPassword
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword()
    private val validateOtp: ValidateOtp = ValidateOtp()

    private var _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ForgotPasswordEvent) = viewModelScope.launch {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
                validateEmail()
            }

            is ForgotPasswordEvent.OtpChanged -> {
                _uiState.update { it.copy(otp = event.otp) }
            }

            is ForgotPasswordEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
                validatePassword()
            }

            is ForgotPasswordEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
                validateRepeatedPassword()
            }

            is ForgotPasswordEvent.PasswordVisibilityChanged -> {
                _uiState.update { it.copy(isPasswordVisible = event.isPasswordVisible) }
            }

            is ForgotPasswordEvent.VisibilityChanged -> {
                _uiState.update { it.copy(isOtpViewVisible = event.visible) }
            }

            is ForgotPasswordEvent.Submit -> {
                if (_uiState.value.isOtpVerified) {
                    // change password
                } else if (_uiState.value.isOtpViewVisible) {
                    if (validateOtp()) {
                        verifyOTP(uiState.value.otp.toInt())
                    }
                } else if (validateEmail()) {
                    authRepo.forgotPassword(_uiState.value.email).collectLatest {
                        when (it) {
                            is Resource.Loading -> Unit
                            is Resource.Success -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        submitButtonText = "Verify OTP",
                                        isOtpViewVisible = true,
                                        toastMessage = "Please check your email for otp"
                                    )
                                }
                            }

                            is Resource.Error -> _uiState.update { currentState ->
                                currentState.copy(
                                    toastMessage = it.message
                                )
                            }
                        }
                    }
                }
            }

            is ForgotPasswordEvent.ResendOTP -> {}
        }
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

    private fun validateRepeatedPassword(): Boolean {
        val passwordResult = validateRepeatedPassword.execute(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )
        _uiState.update { it.copy(confirmPasswordError = passwordResult.errorMessage) }
        return passwordResult.successful
    }

    private fun validateOtp(): Boolean {
        val otpResult = validateOtp.execute(_uiState.value.otp)
        _uiState.update { it.copy(otpError = otpResult.errorMessage) }
        return otpResult.successful
    }

    private suspend fun verifyOTP(otp: Int) {
        authRepo.verifyOTP(userId = "", otp = otp, type = "FORGOT").collectLatest {
            when (it) {
                is Resource.Loading -> ""
                is Resource.Success -> _uiState.update { currentState ->
                    currentState.copy(submitButtonText = "Update Password")
                }

                is Resource.Error -> ""
            }
        }
    }
}