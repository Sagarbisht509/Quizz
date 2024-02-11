package com.sagar.quizz.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.utill.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizzDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllQuizzes(): Flow<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<Quiz>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllQuizzes()

    @Query("UPDATE $TABLE_NAME SET voteCount = :voteCount, upVotedBy = :upVotedList WHERE id = :quizId")
    suspend fun updateQuiz(quizId: String, voteCount: Int, upVotedList: List<String>)

    @Query("SELECT * FROM $TABLE_NAME WHERE title LIKE :query OR category IN (:category)")
    fun getQuizzesByQueryAndCategory(query: String, category: List<String>): Flow<List<Quiz>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:quizId")
    fun getQuiz(quizId: String): Flow<Quiz>

    @Query("SELECT * FROM $TABLE_NAME WHERE quizCode=:quizCode")
    fun getQuizByCode(quizCode: String): Flow<Quiz?>
}