package com.sagar.quizz.presentation.onBoarding

import com.sagar.quizz.R

sealed class OnBoardingPage(
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.img,
        title = "Welcome to Quizz",
        description = "Embark on an exciting journey of knowledge and entertainment with Quizz! Get ready to explore, learn, and have fun through a world of quizzes that await you."
    )

    object Second : OnBoardingPage(
        image = R.drawable.img,
        title = "Easy Quiz Creation",
        description = "Craft and Share Quizzes Effortlessly with Quizz! Create engaging quizzes in just a few clicks and share your knowledge, fun, and creativity with the world."
    )

    object Third : OnBoardingPage(
        image = R.drawable.img,
        title = "Easy Quiz Creation",
        description = "Craft and Share Quizzes Effortlessly with Quizz! Create engaging quizzes in just a few clicks and share your knowledge, fun, and creativity with the world."
    )
}
