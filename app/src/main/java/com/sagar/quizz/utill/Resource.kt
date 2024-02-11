package com.sagar.quizz.utill

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: Resource<T>()

    class Success<T> (data: T? = null, message: String = ""): Resource<T>(data = data, message = message)

    class Error<T> (message: String): Resource<T>(message = message)
}
