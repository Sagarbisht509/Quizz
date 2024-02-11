package com.sagar.quizz.data.model.remote.quiz

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sagar.quizz.utill.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Quiz(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val _id: String = "",
    val title: String = "",
    val category: String = "",
    val author: String = "",
    val questions: List<Question> = emptyList(),
    val userId: String = "",
    val quizCode: String = "",
    val voteCount: String = "",
    val upVotedBy: List<String> = emptyList()
)