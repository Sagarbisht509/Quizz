package com.sagar.quizz.data.local.dataStore

import com.sagar.quizz.data.model.remote.auth.User
import kotlinx.coroutines.flow.Flow

interface LocalDataStore {

    suspend fun saveOnBoardingState(completed: Boolean)

    suspend fun readOnBoardingState(): Boolean

    suspend fun saveLoginState(isLogin: Boolean)

    suspend fun readLoginState(): Boolean

    suspend fun saveToken(token: String)

    fun getToken(): Flow<String>

    suspend fun removeToken()

    suspend fun saveUserData(user: User)

    suspend fun removeUserData()

    fun getUserDataFlow(): Flow<User>

    suspend fun getUserData(): User

    suspend fun saveUserId(userId: String)

    fun getUserId(): Flow<String>

    suspend fun removeUserId()
}