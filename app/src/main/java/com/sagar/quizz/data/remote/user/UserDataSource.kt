package com.sagar.quizz.data.remote.user

import com.sagar.quizz.data.model.remote.user.UpdateUserDetailRequest
import com.sagar.quizz.utill.Resource

interface UserDataSource {

    suspend fun updateFavourites(userId: String, favourites: List<String>): Resource<Unit>

    suspend fun updateUserDetails(userDetailRequest: UpdateUserDetailRequest): Resource<Unit>
}