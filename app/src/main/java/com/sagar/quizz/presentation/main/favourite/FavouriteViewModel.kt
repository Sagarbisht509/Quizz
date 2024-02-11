package com.sagar.quizz.presentation.main.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.data.repo.quiz.QuizRepository
import com.sagar.quizz.data.repo.user.UserRepository
import com.sagar.quizz.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouritesScreenState())
    val uiState = _uiState.asStateFlow()


    private val user =
        userRepository.observeCurrentUser().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val quizzes = user.flatMapLatest {
        it?.let { quizRepository.getAllFavouriteQuizzes(it.favoriteQuizIds) } ?: emptyFlow()
    }

    init {
        collectFavouriteQuizzes()
    }

    private fun collectFavouriteQuizzes() = viewModelScope.launch {
        quizzes.collectLatest {
            _uiState.update { currentState -> currentState.copy(quizzes = it) }
        }
    }

    fun quizIdChanged(id: String?) = viewModelScope.launch {
        _uiState.update { it.copy(quizId = id) }
    }

    fun onRemoveQuiz(quizId: String) = viewModelScope.launch {
        removeFromFavourite(quizId)
        quizIdChanged(id = null)
    }

    private suspend fun removeFromFavourite(quizId: String) {
        userRepository.removeFromFavourite(quizId = quizId).collectLatest {
            when (it) {
                is Resource.Loading -> Unit
                is Resource.Error -> _uiState.update { currentState ->
                    currentState.copy(message = it.message)
                }
                is Resource.Success -> _uiState.update { currentState ->
                    currentState.copy(message = "Successfully removed from favourites")
                }
            }
        }
    }
}