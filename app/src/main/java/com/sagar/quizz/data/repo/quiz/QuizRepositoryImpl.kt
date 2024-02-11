package com.sagar.quizz.data.repo.quiz

import com.sagar.quizz.data.local.room.QuizzDao
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.data.remote.quiz.QuizDataSource
import com.sagar.quizz.utill.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val quizDataSource: QuizDataSource,
    private val dao: QuizzDao,
) : QuizRepository {

    override suspend fun fetchAllQuizzes(): Flow<Resource<List<Quiz>>> = flow {
        emit(value = Resource.Loading())
        val response = quizDataSource.getAllQuizzes()
        if (response is Resource.Success) {
            dao.insertQuizzes(quizzes = response.data!!.shuffled())
        }
        emit(value = response)
    }

    override suspend fun getAllQuizzes(
        query: String,
        category: List<String>
    ): Flow<List<Quiz>> = dao.getQuizzesByQueryAndCategory(query = query, category = category)

    override suspend fun getAllFavouriteQuizzes(favourites: List<String>): Flow<List<Quiz>> =
        dao.getAllQuizzes()
            .map {
                it.filter { quiz -> quiz._id in favourites }
            }.flowOn(Dispatchers.IO)

    override suspend fun getQuizByCode(quizCode: String): Flow<Quiz?> =
        dao.getQuizByCode(quizCode = quizCode)

    override suspend fun createQuiz(quiz: Quiz): Flow<Resource<Quiz>> = flow {
        emit(value = Resource.Loading())
        emit(value = quizDataSource.createQuiz(quiz = quiz))
    }

    override suspend fun updateQuiz(quizId: String, quiz: Quiz): Flow<Resource<Quiz>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuiz(quizId: String): Flow<Resource<Quiz>> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuiz(quizId: String): Flow<Quiz> = dao.getQuiz(quizId = quizId)

    override suspend fun getTopRatedQuiz(): Flow<Resource<List<Quiz>>> = flow {
        emit(Resource.Loading())
        emit(quizDataSource.getTopRatedQuiz())
    }

    override suspend fun upvoteQuiz(userId: String, quiz: Quiz): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val resource = quizDataSource.upvoteQuiz(quizId = quiz._id)
        val updatedCount = quiz.voteCount.toInt().plus(1)
        val updatedUpVotedList = quiz.upVotedBy.toMutableList().apply { add(userId) }
        if (resource is Resource.Success) {
            dao.updateQuiz(
                quizId = quiz._id,
                voteCount = updatedCount,
                upVotedList = updatedUpVotedList
            )
        }

        emit(resource)
    }

    override suspend fun getQuizzesCreatedByUser(userId: String) =
        dao.getAllQuizzes().map { quizzes ->
            quizzes.filter { quiz ->
                quiz.userId == userId
            }
        }

    override suspend fun getNumberOfQuizzesCreatedByUser(userId: String): Flow<Int> =
        getQuizzesCreatedByUser(userId).map {
            it.count()
        }

}