package com.sagar.quizz.presentation.quiz.playQuiz

import com.sagar.quizz.data.model.remote.quiz.Quiz

data class PlayQuizState(
    val quizId: String = "",
    val quiz: Quiz = Quiz(),
    val currentQuestionIndex: Int = 0,
    val currentOptionSelected: Int = 5,
    val timeLeft: Long = 0L,
    val isResultsShowing: Boolean = false,
    val isLoading: Boolean = false,
    val isFavourite: Boolean = false,
    val upVoted: Boolean = false,

    val isQuizFinished: Boolean = false,

    val correctAnswers: Int = 0,
) {
    val currentQuestionTimeLeft: String
        get() = "${timeLeft / 1000} sec left"

    val questionCount: Int
        get() = quiz.questions.size

    val progress: Float
        get() = (currentQuestionIndex).toFloat() / questionCount.toFloat()

    val questionCountText: String
        get() = "Q ${currentQuestionIndex + 1} / $questionCount"

    val question: String
        get() = quiz.questions[currentQuestionIndex].question

    val options: List<String>
        get() = quiz.questions[currentQuestionIndex].options

    val answer: Int
        get() = quiz.questions[currentQuestionIndex].answer
}
