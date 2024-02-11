package com.sagar.quizz.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sagar.quizz.data.model.remote.quiz.Quiz

@Database(
    entities = [Quiz::class],
    version = 1
)
@TypeConverters(QuizzTypeConverters::class)
abstract class QuizzDatabase : RoomDatabase() {

    abstract fun getDao() : QuizzDao

}