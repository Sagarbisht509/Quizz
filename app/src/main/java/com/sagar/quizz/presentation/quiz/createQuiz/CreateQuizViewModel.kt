package com.sagar.quizz.presentation.quiz.createQuiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.local.dataStore.LocalDataStore
import com.sagar.quizz.data.model.remote.auth.User
import com.sagar.quizz.data.model.remote.quiz.Question
import com.sagar.quizz.data.model.remote.quiz.Quiz
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
class CreateQuizViewModel @Inject constructor(
    private val quizRepo: QuizRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _quizUiState = MutableStateFlow(CreateQuizState())
    val quizUiState = _quizUiState.asStateFlow()

    private val _questionUiState = MutableStateFlow(QuestionState())
    val questionState = _questionUiState.asStateFlow()

    fun onQuizEvent(event: CreateQuizEvent) = viewModelScope.launch {
        when (event) {
            is CreateQuizEvent.TitleChanged -> {
                _quizUiState.update { it.copy(title = event.title, titleError = null) }
            }

            is CreateQuizEvent.CategoryChanged -> {
                _quizUiState.update {
                    it.copy(
                        category = event.category,
                        categoryError = null
                    )
                }
            }

            is CreateQuizEvent.Expended -> {
                _quizUiState.update { it.copy(isExpended = event.isExpended) }
            }

            is CreateQuizEvent.CreateQuiz -> {
                if (isQuizFieldsNotEmpty()) {
                    val quiz = getQuizFromStates(quizUiState.value, localDataStore.getUserData())
                    saveQuiz(quiz)
                }
            }

            is CreateQuizEvent.RemoveQuestion -> {
                val updatedQuestions = quizUiState.value.questions.toMutableList().apply {
                    remove(event.question)
                }

                _quizUiState.update { it.copy(questions = updatedQuestions) }
            }

            is CreateQuizEvent.HoldRemovedQuestion -> {
                _quizUiState.update { it.copy(recentRemovedQuestion = event.question) }
            }

            is CreateQuizEvent.UndoRemovedQuestion -> {
                val updatedQuestions = quizUiState.value.questions.toMutableList().apply {
                    add(event.question)
                }

                _quizUiState.update { it.copy(questions = updatedQuestions) }
            }

            is CreateQuizEvent.BottomSheetStateChanged -> {
                _quizUiState.update { it.copy(showBottomSheet = event.isOpened) }
            }
        }
    }

    fun onQuestionEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.QuestionChanged -> {
                _questionUiState.update { it.copy(question = event.question) }
            }

            is QuestionEvent.OptionChanged -> {
                val updatedOptions = questionState.value.options.toMutableList().apply {
                    set(event.index, event.option)
                }

                _questionUiState.update { it.copy(options = updatedOptions) }
            }

            is QuestionEvent.AnswerChanged -> {
                _questionUiState.update { it.copy(answer = event.answer) }
            }

            is QuestionEvent.DiscardQuestion -> {
                clearQuestionState()
            }

            is QuestionEvent.SaveQuestion -> {
                if (isAllFieldsNotEmpty()) {
                    val question = Question(
                        question = questionState.value.question,
                        options = questionState.value.options,
                        answer = questionState.value.answer
                    )
                    val newQuestions = quizUiState.value.questions.toMutableList().apply {
                        add(question)
                    }

                    _quizUiState.update { it.copy(questions = newQuestions) }
                    clearQuestionState()
                }
            }
        }
    }

    private suspend fun saveQuiz(quiz: Quiz) {
        quizRepo.createQuiz(quiz).collectLatest {
            when (it) {
                is Resource.Error -> Unit
                is Resource.Success -> _quizUiState.update { currentState ->
                    currentState.copy(quizCreated = true, isLoading = false)
                }

                is Resource.Loading -> _quizUiState.update { currentState ->
                    currentState.copy(isLoading = true)
                }
            }
        }
    }

    private fun getQuizFromStates(state: CreateQuizState, user: User): Quiz = Quiz(
        title = state.title,
        category = state.category,
        author = user.username,
        questions = state.questions
    )

    private fun clearQuestionState() {
        _questionUiState.update {
            it.copy(
                question = "",
                isQuestionEmpty = false,
                options = listOf("", "", "", ""),
                isOptionsEmpty = listOf(false, false, false, false),
                answer = 5,
                isAnswerEmpty = false
            )
        }
    }

    private fun isAllFieldsNotEmpty(): Boolean {

        var res = true

        if (_questionUiState.value.question.isBlank()) {
            _questionUiState.update { it.copy(isQuestionEmpty = true) }
            res = false
        }

        _questionUiState.value.options.forEachIndexed { index, option ->
            if (option.isEmpty() || option.isBlank()) {

                val updatedOptions =
                    questionState.value.isOptionsEmpty.toMutableList().apply {
                        set(index, true)
                    }

                _questionUiState.update { it.copy(isOptionsEmpty = updatedOptions) }

                res = false
            }
        }

        if (_questionUiState.value.answer == 5) {
            _questionUiState.update { it.copy(isAnswerEmpty = true) }
            res = false
        }

        return res
    }

    private fun isQuizFieldsNotEmpty(): Boolean {
        var res = true

        if (_quizUiState.value.title.isEmpty()) {
            _quizUiState.update { it.copy(titleError = "Field can't be empty") }
            res = false
        }

        if (_quizUiState.value.category.isEmpty()) {
            _quizUiState.update { it.copy(categoryError = "Field can't be empty") }
            res = false
        }

        if (_quizUiState.value.questions.isEmpty()) // show toast
            res = false

        return res
    }
}