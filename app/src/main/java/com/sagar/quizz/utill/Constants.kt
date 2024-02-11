package com.sagar.quizz.utill

import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Category

object Constants {

    const val BASE_URL = "" // Replace With Yours
    const val DATASTORE_NAME = "QuizzDataStore"

    const val OTP_TIME = 300 * 1000L
    const val GAME_TIME = 30 * 1000L

    const val DATABASE_NAME = "quizz_db"
    const val TABLE_NAME = "quizz_table"

    val categoryList = listOf(
        Category(name = CategoryEnum.Technology.categoryName),
        Category(name = CategoryEnum.GeneralKnowledge.categoryName),
        Category(name = CategoryEnum.CurrentAffairs.categoryName),
        Category(name = CategoryEnum.History.categoryName),
        Category(name = CategoryEnum.Science.categoryName),
        Category(name = CategoryEnum.Sports.categoryName),
        Category(name = CategoryEnum.Movies.categoryName),
        Category(name = CategoryEnum.VideoGames.categoryName),
        Category(name = CategoryEnum.MathAndMathematics.categoryName),
        Category(name = CategoryEnum.FoodAndCooking.categoryName)
    )

    enum class CategoryEnum(val categoryName: String) {
        Technology("Technology"),
        GeneralKnowledge("General Knowledge"),
        CurrentAffairs("Current Affairs"),
        History("History"),
        Science("Science"),
        Sports("Sports"),
        Movies("Movies"),
        VideoGames("Video Games"),
        MathAndMathematics("Math and Mathematics"),
        FoodAndCooking("Food and Cooking")
    }

    fun getQuizImage(category: String) : Int {
        return when (category) {
            CategoryEnum.Technology.categoryName -> R.drawable.technology
            CategoryEnum.GeneralKnowledge.categoryName -> R.drawable.general_knowledge
            CategoryEnum.CurrentAffairs.categoryName -> R.drawable.current_affairs
            CategoryEnum.History.categoryName -> R.drawable.history
            CategoryEnum.Science.categoryName -> R.drawable.science
            CategoryEnum.Sports.categoryName -> R.drawable.sports
            CategoryEnum.Movies.categoryName -> R.drawable.movie
            CategoryEnum.VideoGames.categoryName -> R.drawable.quiz_image
            CategoryEnum.MathAndMathematics.categoryName -> R.drawable.math
            CategoryEnum.FoodAndCooking.categoryName -> R.drawable.cooking
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }

    val avatars = listOf(
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar.png?alt=media&token=af2e2647-5a61-42b7-9b62-adc43dad6310",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-1.png?alt=media&token=c4412535-9a4a-46cf-9c60-b212a5ede20d",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-2.png?alt=media&token=b27caeea-2f06-4079-85ed-3b8254a7269f",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-3.png?alt=media&token=b1adcd2b-cba9-4356-8dd6-9149de7cc2d6",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-4.png?alt=media&token=e791c1ed-3381-4ace-8748-3bb1bf98a7ae",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-5.png?alt=media&token=f252d0c4-8945-471f-a7b7-9e40e21af1dc",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-6.png?alt=media&token=9e08a460-a8a3-4f31-a2cd-18cc21982966",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-7.png?alt=media&token=3ac74d18-ebd2-4c6d-89a4-4349e18b6ff8",
        "https://firebasestorage.googleapis.com/v0/b/quizzify-c1e0b.appspot.com/o/Avatar-8.png?alt=media&token=dda6e942-14bd-495d-8020-4c73fd1ec7ab"
    )
}