package com.sagar.quizz.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.repo.quiz.QuizRepository
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepo: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: HomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is HomeScreenEvent.QuizCodeStateChanged -> {
                _uiState.update { it.copy(openAlertDialog = event.value) }
            }

            is HomeScreenEvent.QuizCodeChanged -> {
                _uiState.update { it.copy(quizCode = event.quizCode) }
            }

            is HomeScreenEvent.JoinQuizButtonClicked -> {
                getQuizByCode()
            }
        }
    }

    private suspend fun getQuizByCode() {
        quizRepo.getQuizByCode(quizCode = uiState.value.quizCode).collectLatest { quiz ->
            _uiState.update {
                if (quiz == null) {
                    it.copy(quizCode = "", errorMessage = "No quiz found with the provided code")
                } else {
                    it.copy(quizCode = "", quizId = quiz._id)
                }
            }
        }
    }

    fun removeDataFromState() = viewModelScope.launch {
        _uiState.update { it.copy(quizId = "", errorMessage = null) }
    }

    init {
        onRefreshed()
        getTopRatedQuiz()
    }

    fun onRefreshed() = viewModelScope.launch {
        quizRepo.fetchAllQuizzes().collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> _uiState.update { state -> state.copy(isLoading = true) }
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            topRatedQuiz = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun getTopRatedQuiz() = viewModelScope.launch {
        quizRepo.getTopRatedQuiz().collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> _uiState.update { state -> state.copy(isLoading = true) }
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            topRatedQuiz = it.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}