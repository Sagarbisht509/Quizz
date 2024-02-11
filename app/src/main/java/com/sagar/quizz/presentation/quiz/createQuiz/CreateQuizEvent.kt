package com.sagar.quizz.presentation.quiz.createQuiz

import com.sagar.quizz.data.model.remote.quiz.Question

sealed class CreateQuizEvent {
    data class TitleChanged(val title: String): CreateQuizEvent()
    data class CategoryChanged(val category: String): CreateQuizEvent()
    data class BottomSheetStateChanged(val isOpened: Boolean): CreateQuizEvent()
    data class Expended(val isExpended: Boolean): CreateQuizEvent()
    data class RemoveQuestion(val question: Question): CreateQuizEvent()
    data class HoldRemovedQuestion(val question: Question): CreateQuizEvent()
    data class UndoRemovedQuestion(val question: Question): CreateQuizEvent()

    object CreateQuiz: CreateQuizEvent()
}

sealed class QuestionEvent {
    data class QuestionChanged(val question: String): QuestionEvent()
    data class OptionChanged(val index: Int, val option: String): QuestionEvent()
    data class AnswerChanged(val answer: Int): QuestionEvent()

    object SaveQuestion: QuestionEvent()
    object DiscardQuestion: QuestionEvent()
}