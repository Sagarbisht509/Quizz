package com.sagar.quizz.data.repo.quiz

import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.utill.Resource
import kotlinx.coroutines.flow.Flow

interface QuizRepository {

    suspend fun fetchAllQuizzes(): Flow<Resource<List<Quiz>>>

    suspend fun getAllQuizzes(query: String, category: List<String>): Flow<List<Quiz>>

    suspend fun getAllFavouriteQuizzes(favourites: List<String>): Flow<List<Quiz>>

    suspend fun getQuizByCode(quizCode: String): Flow<Quiz?>

    suspend fun createQuiz(quiz: Quiz): Flow<Resource<Quiz>>

    suspend fun updateQuiz(quizId: String, quiz: Quiz): Flow<Resource<Quiz>>

    suspend fun deleteQuiz(quizId: String): Flow<Resource<Quiz>>

    suspend fun getQuiz(quizId: String): Flow<Quiz>

    suspend fun getTopRatedQuiz(): Flow<Resource<List<Quiz>>>

    suspend fun upvoteQuiz(userId: String, quiz: Quiz): Flow<Resource<Unit>>

    suspend fun getQuizzesCreatedByUser(userId: String): Flow<List<Quiz>>

    suspend fun getNumberOfQuizzesCreatedByUser(userId: String): Flow<Int>
}