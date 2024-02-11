package com.sagar.quizz.data.repo.user

import com.sagar.quizz.data.local.dataStore.LocalDataStore
import com.sagar.quizz.data.model.remote.auth.User
import com.sagar.quizz.data.model.remote.user.UpdateUserDetailRequest
import com.sagar.quizz.data.remote.user.UserDataSource
import com.sagar.quizz.utill.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataStore: LocalDataStore,
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun observeCurrentUser(): Flow<User> =
        localDataStore.getUserDataFlow().flowOn(Dispatchers.IO)

    override suspend fun getCurrentUser() = localDataStore.getUserData()

    override suspend fun updateAvatar(avatar: String): Flow<Resource<Unit>> = flow {
        emit(value = Resource.Loading())
        val user = getCurrentUser()
        val newUser = user.copy(avatar = avatar)
        emit(updateUserDetails(newUser))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateExpAndQuizAttempt(exp: Int): Flow<Resource<Unit>> =
        flow {
            emit(value = Resource.Loading())
            val user = getCurrentUser()
            val newExp = user.exp.plus(exp)
            val newQuizAttempt = user.quizAttempt.plus(1)
            val newUser = user.copy(exp = newExp, quizAttempt = newQuizAttempt)
            emit(updateUserDetails(newUser))

        }.flowOn(Dispatchers.IO)

    override suspend fun addToFavourite(quizId: String): Flow<Resource<Unit>> = flow {
        emit(value = Resource.Loading())
        val user = getCurrentUser()
        val newFavourites = if (user.favoriteQuizIds == null) listOf(quizId)
        else user.favoriteQuizIds.toMutableList().apply { add(quizId) }
        val newUser = user.copy(favoriteQuizIds = newFavourites.toList())
        emit(updateFavourites(newUser))
    }.flowOn(Dispatchers.IO)

    override suspend fun removeFromFavourite(quizId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = getCurrentUser()
        val newFavourites = user.favoriteQuizIds.toMutableList().apply { remove(quizId) }
        val newUser = user.copy(favoriteQuizIds = newFavourites.toList())
        emit(updateFavourites(newUser))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateUserDetails(user: User): Resource<Unit> {
        val resource = userDataSource.updateUserDetails(
            UpdateUserDetailRequest(
                userId = user._id,
                exp = user.exp,
                avatar = user.avatar,
                quizAttempt = user.quizAttempt
            )
        )
        if (resource is Resource.Success) {
            localDataStore.saveUserData(user)
        }
        return resource
    }

    private suspend fun updateFavourites(user: User): Resource<Unit> {
        val resource = userDataSource.updateFavourites(user._id, user.favoriteQuizIds)
        if (resource is Resource.Success) {
            localDataStore.saveUserData(user)
        }
        return resource
    }
}