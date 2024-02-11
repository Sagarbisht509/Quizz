package com.sagar.quizz.di

import com.sagar.quizz.data.local.dataStore.LocalDataStore
import com.sagar.quizz.data.local.dataStore.LocalDataStoreImpl
import com.sagar.quizz.data.remote.auth.AuthDataSource
import com.sagar.quizz.data.remote.auth.AuthDataSourceImpl
import com.sagar.quizz.data.remote.quiz.QuizDataSource
import com.sagar.quizz.data.remote.quiz.QuizDataSourceImpl
import com.sagar.quizz.data.remote.user.UserDataSource
import com.sagar.quizz.data.remote.user.UserDataSourceImpl
import com.sagar.quizz.data.repo.auth.AuthRepository
import com.sagar.quizz.data.repo.auth.AuthRepositoryImpl
import com.sagar.quizz.data.repo.quiz.QuizRepository
import com.sagar.quizz.data.repo.quiz.QuizRepositoryImpl
import com.sagar.quizz.data.repo.user.UserRepository
import com.sagar.quizz.data.repo.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthInterfaceModules {

    @Binds
    abstract fun bindsDataStore(
        localDataStoreImpl: LocalDataStoreImpl
    ): LocalDataStore

    @Binds
    abstract fun bindsAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindsQuizDataSource(
        quizDataSourceImpl: QuizDataSourceImpl
    ): QuizDataSource

    @Binds
    abstract fun bindsQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ): QuizRepository

    @Binds
    abstract fun bindsUserDataSource(
        userDataSourceImpl: UserDataSourceImpl
    ): UserDataSource

    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}