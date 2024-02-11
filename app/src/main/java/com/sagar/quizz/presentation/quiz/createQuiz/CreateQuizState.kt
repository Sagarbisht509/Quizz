package com.sagar.quizz.presentation.quiz.createQuiz

import com.sagar.quizz.data.model.remote.quiz.Question

data class CreateQuizState(
    val title: String = "",
    val titleError: String? = null,
    val category: String = "",
    val categoryError: String? = null,
    val questions: List<Question> = emptyList(),
    val recentRemovedQuestion: Question? = null,

    val isLoading: Boolean = false,
    val quizCreated: Boolean = false,
    val isExpended: Boolean = false,
    val showBottomSheet: Boolean = false
)

data class QuestionState(
    val question: String = "",
    val isQuestionEmpty: Boolean = false,
    val options: List<String> = listOf("", "", "", ""),
    val isOptionsEmpty: List<Boolean> = listOf(false, false, false, false),
    val answer: Int = 5,
    val isAnswerEmpty: Boolean = false,
) {
    val showSaveButton: Boolean
        get() = question.isNotBlank() && answer != 5 && !options.any { it.isBlank() }
}
