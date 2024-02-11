package com.sagar.quizz.data.remote.api

import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.model.remote.auth.AuthResponse
import com.sagar.quizz.data.model.remote.user.UpdateUserDetailRequest
import com.sagar.quizz.data.model.remote.user.UserFavoriteQuizRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAPI {

    @POST("users/signin")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("users/signup")
    suspend fun signUp(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("users/verifyOTP")
    suspend fun verifyOTP(
        @Query("userId") userId: String,
        @Query("otp") otp: Int,
        @Query("type") type: String,
    ): Response<Unit>

    @POST("users/resendOTP")
    suspend fun resendOTP(
        @Query("userId") userId: String,
        @Query("type") type: String
    ): Response<Unit>

    @POST("users/forgotPassword")
    suspend fun forgotPassword(
        @Query("email") email: String
    ): Response<Unit>

    @PUT("users/updateFavoriteQuiz")
    suspend fun updateFavoriteQuiz(
        @Body userFavoriteQuizRequest: UserFavoriteQuizRequest
    ): Response<Unit>

    @PUT("users/updateDetails")
    suspend fun updateUserDetails(
        @Body updateUserDetailRequest: UpdateUserDetailRequest
    ): Response<Unit>
}