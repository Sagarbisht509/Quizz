package com.sagar.quizz.utill

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    successMessage: String = "",
    errorMessage: String? = null,
    call: suspend () -> Response<T>
): Resource<T> = try {
    val response = call()
    if (response.isSuccessful) {
        response.body()?.let { result -> Resource.Success(data = result, message = successMessage) }
            ?: Resource.Error(message = errorMessage ?: response.message())
    } else {
        Resource.Error(message = errorMessage ?: response.message())
    }
} catch (e: IOException) {
    Resource.Error(message = "Please check your network connection")
} catch (e: Exception) {
    Resource.Error(message = errorMessage ?: e.message.toString())
}