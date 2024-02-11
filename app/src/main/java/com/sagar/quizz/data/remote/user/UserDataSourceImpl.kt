package com.sagar.quizz.data.remote.user

import com.sagar.quizz.data.model.remote.user.UpdateUserDetailRequest
import com.sagar.quizz.data.model.remote.user.UserFavoriteQuizRequest
import com.sagar.quizz.data.remote.api.UserAPI
import com.sagar.quizz.utill.Resource
import com.sagar.quizz.utill.safeApiCall
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userAPI: UserAPI
) : UserDataSource {

    override suspend fun updateFavourites(
        userId: String,
        favourites: List<String>
    ): Resource<Unit> =
        safeApiCall(
            call = { userAPI.updateFavoriteQuiz(UserFavoriteQuizRequest(userId, favourites)) },
            successMessage = "Successfully updated",
            errorMessage = "Failed to update"
        )

    override suspend fun updateUserDetails(
        userDetailRequest: UpdateUserDetailRequest
    ): Resource<Unit> =
        safeApiCall(
            call = { userAPI.updateUserDetails(userDetailRequest) },
            successMessage = "Successfully updated",
            errorMessage = "Failed to update"
        )

}