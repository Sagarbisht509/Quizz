package com.sagar.quizz.presentation.quiz.playQuiz

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.repo.quiz.QuizRepository
import com.sagar.quizz.data.repo.user.UserRepository
import com.sagar.quizz.utill.Constants.GAME_TIME
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayQuizState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: PlayQuizEvent) = viewModelScope.launch {
        when (event) {

            is PlayQuizEvent.OnOptionSelected -> {
                _uiState.update { it.copy(currentOptionSelected = event.selectedOption) }
            }

            is PlayQuizEvent.SkipButtonClicked -> {
                _uiState.update { it.copy(currentOptionSelected = 5) }
                submitAnswer()
            }

            is PlayQuizEvent.NextButtonClicked -> {
                submitAnswer()
                restartTimer()
            }
        }
    }

    private fun submitAnswer() = viewModelScope.launch {
        val isCorrectOptionSelected = uiState.value.currentOptionSelected == uiState.value.answer
        if (isCorrectOptionSelected) {
            _uiState.update { it.copy(correctAnswers = uiState.value.correctAnswers.plus(1)) }
        }
        _uiState.update { it.copy(isResultsShowing = true) }
        delay(2000L)

        if (uiState.value.currentQuestionIndex + 1 == uiState.value.questionCount) {
            _uiState.update { it.copy(isQuizFinished = true) }
            updateExpAndQuizAttempt()
        } else {
            _uiState.update {
                it.copy(currentQuestionIndex = uiState.value.currentQuestionIndex.plus(1))
            }
        }

        _uiState.update { it.copy(isResultsShowing = false, currentOptionSelected = 5) }
    }

    private val timer = object : CountDownTimer(GAME_TIME, 1000L) {
        override fun onTick(p0: Long) {
            _uiState.update { it.copy(timeLeft = p0) }
        }

        override fun onFinish() {
            _uiState.update { it.copy(timeLeft = 0L) }
            onTimerEnd()
        }
    }

    private fun restartTimer() = viewModelScope.launch {
        _uiState.update { it.copy(timeLeft = 0L) }
        timer.apply {
            cancel()
            start()
        }
    }

    private fun onTimerEnd() = viewModelScope.launch { submitAnswer() }

    fun startQuiz() = viewModelScope.launch { timer.start() }

    fun setQuizId(quizId: String) = viewModelScope.launch {
        _uiState.update { it.copy(quizId = quizId) }
        quizRepository.getQuiz(quizId).collectLatest { quiz ->
            _uiState.update { it.copy(quiz = quiz) }
        }
    }

    fun collectIsQuizUserFav() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isFavourite = userRepository.getCurrentUser().favoriteQuizIds.contains(
                    uiState.value.quizId
                )
            )
        }
    }

    fun onFavouriteIconClicked() = viewModelScope.launch { favOrUnFavQuiz() }

    private suspend fun favOrUnFavQuiz() {
        val isFavourite = uiState.value.isFavourite
        val quizId = uiState.value.quizId
        val flow = if (isFavourite) userRepository.removeFromFavourite(quizId = quizId)
        else userRepository.addToFavourite(quizId = quizId)
        flow.collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> _uiState.update { currentState ->
                    currentState.copy(isLoading = true)
                }

                is Resource.Success -> _uiState.update { currentState ->
                    currentState.copy(isFavourite = !isFavourite, isLoading = false)
                }
            }
        }
    }

    private suspend fun updateExpAndQuizAttempt() {
        userRepository.updateExpAndQuizAttempt(exp = uiState.value.correctAnswers * 2)
            .collectLatest {
                when (it) {
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                    is Resource.Success -> Unit
                }
            }
    }

    fun onUpVoteButtonClicked() = viewModelScope.launch {
        upvoteQuiz()
    }

    private suspend fun upvoteQuiz() {
        quizRepository.upvoteQuiz(
            userId = userRepository.getCurrentUser()._id,
            quiz = uiState.value.quiz
        ).collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Loading -> Unit
                is Resource.Success -> _uiState.update { currentState ->
                    currentState.copy(upVoted = true)
                }
            }
        }
    }

}