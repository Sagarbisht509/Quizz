package com.sagar.quizz.data.remote.api

import com.sagar.quizz.data.local.dataStore.LocalDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStore: LocalDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // Use runBlocking to collect the token synchronously
        val token = runBlocking {
            dataStore.getToken().first() // Collect the first emission
        }

        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "bar $token")

        return chain.proceed(request = request.build())
    }
}