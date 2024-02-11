package com.sagar.quizz.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.repo.auth.AuthRepository
import com.sagar.quizz.data.repo.quiz.QuizRepository
import com.sagar.quizz.data.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        setUserData()
        getQuizCount()
    }

    private fun setUserData() = viewModelScope.launch {
        userRepository.observeCurrentUser().collectLatest { user ->
            _uiState.update {
                it.copy(
                    email = user.email,
                    userName = user.username,
                    profilePic = user.avatar,
                    exp = user.exp,
                    quizAttempts = user.quizAttempt,
                    userId = user._id
                )
            }
        }
    }

    private fun getQuizCount() = viewModelScope.launch {
        quizRepository.getNumberOfQuizzesCreatedByUser(authRepository.getUserId())
            .collectLatest { count ->
                _uiState.update { it.copy(quizCreated = count) }
            }
    }

    fun changeLogoutDialogState(value: Boolean) = viewModelScope.launch {
        _uiState.update { it.copy(openAlertDialog = value) }
    }

    fun onLogoutConfirmed() = viewModelScope.launch {
        authRepository.logoutUser()
        _uiState.update { it.copy(logoutCompleted = true) }
    }
}