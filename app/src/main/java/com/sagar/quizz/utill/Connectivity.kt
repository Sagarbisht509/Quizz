package com.sagar.quizz.utill

import kotlinx.coroutines.flow.Flow

interface Connectivity {

    enum class Status {
        Available,
        Unavailable
    }

    fun observe(): Flow<Status>
}