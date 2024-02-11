package com.sagar.quizz.data.remote.api

import com.sagar.quizz.data.model.remote.quiz.Quiz
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizAPI {

    @GET("quiz/")
    suspend fun getAllQuizzes(): Response<List<Quiz>>

    @GET("quiz/")
    suspend fun getQuizByCode(@Query("code") quizCode: Number): Response<Quiz>

    @GET("quiz/")
    suspend fun getQuizBYCategories(): Response<List<Quiz>>

    @POST("quiz/")
    suspend fun createQuiz(@Body quiz: Quiz): Response<Quiz>

    @PUT("quiz/{id}")
    suspend fun updateQuiz(@Path("id") quizId: String, quiz: Quiz): Response<Quiz>

    @DELETE("quiz/{id}")
    suspend fun deleteQuiz(@Path("id") quizId: String): Response<Quiz>

    @GET("quiz/top")
    suspend fun getTopRatedQuiz(): Response<List<Quiz>>

    @PUT("quiz/upvote/{id}")
    suspend fun upvoteQuiz(@Path("id") quizId: String): Response<Unit>
}