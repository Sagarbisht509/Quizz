package com.sagar.quizz.presentation.main.profile

data class ProfileState(
    val email: String = "",
    val userName: String = "",
    val profilePic: String = "",
    val quizAttempts: Int = 0,
    val quizCreated: Int = 0,
    val exp: Int = 0,
    val userId: String = "",

    val openAlertDialog: Boolean = false,
    val logoutCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
