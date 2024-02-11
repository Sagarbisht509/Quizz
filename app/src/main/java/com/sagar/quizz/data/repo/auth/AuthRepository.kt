package com.sagar.quizz.data.repo.auth

import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.model.remote.auth.User
import com.sagar.quizz.utill.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun saveUserId(userId: String)

    suspend fun getUserId(): String

    suspend fun removeUserId()

    suspend fun getUserData(): User

    suspend fun loginUser(authRequest: AuthRequest): Flow<Resource<Unit>>

    suspend fun registerUser(authRequest: AuthRequest): Flow<Resource<Unit>>

    suspend fun logoutUser()

    suspend fun verifyOTP(userId: String, otp: Int, type: String): Flow<Resource<Unit>>

    suspend fun resendOTP(userId: String, type: String): Flow<Resource<Unit>>

    suspend fun forgotPassword(email: String): Flow<Resource<Unit>>
}