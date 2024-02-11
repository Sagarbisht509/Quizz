package com.sagar.quizz.data.repo.user

import com.sagar.quizz.data.model.remote.auth.User
import com.sagar.quizz.utill.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeCurrentUser(): Flow<User>

    suspend fun getCurrentUser() : User

    suspend fun updateAvatar(avatar: String): Flow<Resource<Unit>>

    suspend fun updateExpAndQuizAttempt(exp: Int): Flow<Resource<Unit>>

    suspend fun addToFavourite(quizId: String): Flow<Resource<Unit>>

    suspend fun removeFromFavourite(quizId: String): Flow<Resource<Unit>>
}