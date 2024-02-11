package com.sagar.quizz.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.repo.auth.AuthRepository
import com.sagar.quizz.domain.useCase.ValidateEmail
import com.sagar.quizz.domain.useCase.ValidatePassword
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) = viewModelScope.launch {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
                validateEmail()
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
                validatePassword()
            }

            is LoginEvent.PasswordVisibilityChanged -> {
                _uiState.update { it.copy(isPasswordVisible = event.isPasswordVisible) }
            }

            is LoginEvent.LoginButtonClicked -> {
                val isPasswordValid = validatePassword()
                val isEmailValid = validateEmail()
                if (isEmailValid && isPasswordValid) {
                    loginUsingCredentials(
                        AuthRequest(
                            email = _uiState.value.email.trim(),
                            password = _uiState.value.password.trim()
                        )
                    )
                }
            }
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

    private suspend fun loginUsingCredentials(authRequest: AuthRequest) {
        authRepo.loginUser(authRequest).collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> _uiState.update { updatedState -> updatedState.copy(isLoading = true) }
                is Resource.Success -> _uiState.update { updatedState ->
                    updatedState.copy(
                        successfullyLoggedIn = true
                    )
                }
            }
        }
    }

}