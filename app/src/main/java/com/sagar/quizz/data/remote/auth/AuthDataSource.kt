package com.sagar.quizz.data.remote.auth

import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.model.remote.auth.AuthResponse
import retrofit2.Response

interface AuthDataSource {

    suspend fun loginUser(authRequest: AuthRequest): Response<AuthResponse>

    suspend fun registerUser(authRequest: AuthRequest): Response<AuthResponse>

    suspend fun verifyOTP(userId: String, otp: Int, type: String): Response<Unit>

    suspend fun resendOTP(userId: String, type: String): Response<Unit>

    suspend fun forgotPassword(email: String): Response<Unit>

}