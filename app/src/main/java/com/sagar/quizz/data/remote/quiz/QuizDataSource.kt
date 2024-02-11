package com.sagar.quizz.data.remote.quiz

import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.utill.Resource

interface QuizDataSource {

    suspend fun getAllQuizzes(): Resource<List<Quiz>>

    suspend fun getQuizByCode(quizCode: Number): Resource<Quiz>

    suspend fun getQuizBYCategories(): Resource<List<Quiz>>

    suspend fun createQuiz(quiz: Quiz): Resource<Quiz>

    suspend fun updateQuiz(quizId: String, quiz: Quiz): Resource<Quiz>

    suspend fun deleteQuiz(quizId: String): Resource<Quiz>

    suspend fun getTopRatedQuiz(): Resource<List<Quiz>>

    suspend fun upvoteQuiz(quizId: String): Resource<Unit>
}