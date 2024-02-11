package com.sagar.quizz.presentation.quiz.playQuiz

sealed class PlayQuizEvent {

    data class OnOptionSelected(val selectedOption: Int): PlayQuizEvent()

    object SkipButtonClicked: PlayQuizEvent()
    object NextButtonClicked: PlayQuizEvent()
}
