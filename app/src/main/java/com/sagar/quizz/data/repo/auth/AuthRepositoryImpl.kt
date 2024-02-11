package com.sagar.quizz.data.repo.auth

import com.sagar.quizz.data.local.dataStore.LocalDataStore
import com.sagar.quizz.data.model.remote.auth.AuthRequest
import com.sagar.quizz.data.model.remote.auth.AuthResponse
import com.sagar.quizz.data.model.remote.auth.User
import com.sagar.quizz.data.remote.auth.AuthDataSource
import com.sagar.quizz.utill.Resource
import com.sagar.quizz.utill.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val dataStore: LocalDataStore
) : AuthRepository {

    override suspend fun saveUserId(userId: String) = dataStore.saveUserId(userId = userId)

    override suspend fun getUserId(): String = dataStore.getUserId().first()

    override suspend fun removeUserId() = dataStore.removeUserId()

    override suspend fun getUserData(): User = dataStore.getUserData()

    override suspend fun loginUser(authRequest: AuthRequest): Flow<Resource<Unit>> =
        flow<Resource<Unit>> {
            emit(value = Resource.Loading())

            val loginResource = safeApiCall(
                call = { authDataSource.loginUser(authRequest) },
                successMessage = "Successfully logged in"
            )

            if (loginResource is Resource.Success) {
                handleAfterLoginAndRegister(loginResource.data!!)
                emit(value = Resource.Success())
            } else emit(value = Resource.Error(message = loginResource.message!!))
        }.flowOn(Dispatchers.IO)

    override suspend fun registerUser(authRequest: AuthRequest): Flow<Resource<Unit>> =
        flow<Resource<Unit>> {
            emit(value = Resource.Loading())

            val registerResource = safeApiCall(
                call = { authDataSource.registerUser(authRequest) },
                successMessage = "Successfully registered"
            )

            if (registerResource is Resource.Success) {
                handleAfterLoginAndRegister(registerResource.data!!)
                emit(value = Resource.Success())
            } else emit(value = Resource.Error(message = registerResource.message!!))
        }.flowOn(Dispatchers.IO)

    override suspend fun logoutUser() {
        dataStore.apply {
            removeUserData()
            saveLoginState(isLogin = false)
            removeToken()
            removeUserId()
        }
    }

    private suspend fun handleAfterLoginAndRegister(authResponse: AuthResponse) {
        dataStore.apply {
            saveLoginState(isLogin = true)
            saveUserData(user = authResponse.user)
            saveToken(token = authResponse.token)
            saveUserId(userId = authResponse.user._id)
        }
    }

    override suspend fun verifyOTP(userId: String, otp: Int, type: String): Flow<Resource<Unit>> =
        flow {
            val verifyResource = safeApiCall(
                call = { authDataSource.verifyOTP(userId, otp, type) },
                successMessage = "Successfully verified OTP",
                errorMessage = "Failed to verify OTP"
            )

            if (verifyResource is Resource.Success) emit(value = Resource.Success())
            else emit(value = Resource.Error(message = verifyResource.message!!))
        }

    override suspend fun resendOTP(userId: String, type: String): Flow<Resource<Unit>> = flow {
        val resendOTPResource = safeApiCall(
            call = { authDataSource.resendOTP(userId, type) },
            successMessage = "OTP resent successfully",
            errorMessage = "Failed to resend OTP"
        )

        if (resendOTPResource is Resource.Success) emit(Resource.Success())
        else emit(Resource.Error(resendOTPResource.message!!))
    }

    override suspend fun forgotPassword(email: String): Flow<Resource<Unit>> = flow {
        val forgotPassResource = safeApiCall(
            call = { authDataSource.forgotPassword(email) },
            successMessage = "Password recovery initiated successfully",
            errorMessage = "Failed to initiate password recovery"
        )

        if (forgotPassResource is Resource.Success) emit(Resource.Success())
        else emit(Resource.Error(forgotPassResource.message!!))
    }
}