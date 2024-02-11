package com.sagar.quizz.data.remote.auth

import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.model.remote.auth.AuthResponse
import com.sagar.quizz.data.remote.api.UserAPI
import retrofit2.Response
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(private val userAPI: UserAPI) : AuthDataSource {

    override suspend fun loginUser(authRequest: AuthRequest): Response<AuthResponse> =
        userAPI.signIn(authRequest)

    override suspend fun registerUser(authRequest: AuthRequest): Response<AuthResponse> =
        userAPI.signUp(authRequest)

    override suspend fun verifyOTP(
        userId: String,
        otp: Int,
        type: String
    ): Response<Unit> = userAPI.verifyOTP(userId, otp, type)

    override suspend fun resendOTP(userId: String, type: String): Response<Unit> =
        userAPI.resendOTP(userId, type)

    override suspend fun forgotPassword(email: String): Response<Unit> =
        userAPI.forgotPassword(email)
}