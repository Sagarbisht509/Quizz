package com.sagar.quizz.data.model.local

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: Int,
    val badgeCount: Int? = null
)
