package com.sagar.quizz.data.local.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagar.quizz.data.model.remote.quiz.Question
import com.sagar.quizz.data.model.remote.quiz.Quiz
import java.lang.reflect.Type

class QuizzTypeConverters {

    @TypeConverter
    fun fromQuiz(quiz: Quiz): String {
        return Gson().toJson(quiz)
    }

    @TypeConverter
    fun toQuiz(quiz: String): Quiz {
        return Gson().fromJson(quiz, Quiz::class.java)
    }

    @TypeConverter
    fun fromQuestionList(questions: List<Question>) : String {
        val type: Type = object : TypeToken<List<Question?>?>() {}.type
        return Gson().toJson(questions, type)
    }

    @TypeConverter
    fun toQuestionList(questions: String): List<Question> {
        val type: Type = object : TypeToken<List<Question?>?>() {}.type
        return Gson().fromJson(questions, type)
    }

    @TypeConverter
    fun fromList(list: List<String>) : String {
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toList(list: String): List<String> {
        val type: Type = object : TypeToken<List<Question?>?>() {}.type
        return Gson().fromJson(list, type)
    }

}