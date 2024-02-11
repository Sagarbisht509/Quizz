package com.sagar.quizz.data.remote.quiz

import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.data.remote.api.QuizAPI
import com.sagar.quizz.utill.Resource
import com.sagar.quizz.utill.safeApiCall
import javax.inject.Inject

class QuizDataSourceImpl @Inject constructor(private val quizAPI: QuizAPI) : QuizDataSource {

    override suspend fun getAllQuizzes(): Resource<List<Quiz>> =
        safeApiCall(
            call = { quizAPI.getAllQuizzes() }
        )

    override suspend fun getQuizByCode(quizCode: Number): Resource<Quiz> =
        safeApiCall(
            call = { quizAPI.getQuizByCode(quizCode = quizCode) }
        )

    override suspend fun getQuizBYCategories(): Resource<List<Quiz>> =
        safeApiCall(
            call = { quizAPI.getQuizBYCategories() }
        )

    override suspend fun createQuiz(quiz: Quiz): Resource<Quiz> =
        safeApiCall(
            call = { quizAPI.createQuiz(quiz = quiz) }
        )

    override suspend fun updateQuiz(quizId: String, quiz: Quiz): Resource<Quiz> =
        safeApiCall(
            call = { quizAPI.updateQuiz(quizId, quiz) }
        )

    override suspend fun deleteQuiz(quizId: String): Resource<Quiz> =
        safeApiCall(
            call = { quizAPI.deleteQuiz(quizId = quizId) }
        )

    override suspend fun getTopRatedQuiz(): Resource<List<Quiz>> =
        safeApiCall(
            call = { quizAPI.getTopRatedQuiz() }
        )

    override suspend fun upvoteQuiz(quizId: String): Resource<Unit> =
        safeApiCall(
            call = { quizAPI.upvoteQuiz(quizId = quizId) }
        )
}