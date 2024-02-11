package com.sagar.quizz.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.sagar.quizz.data.local.room.QuizzDao
import com.sagar.quizz.data.local.room.QuizzDatabase
import com.sagar.quizz.data.remote.api.AuthInterceptor
import com.sagar.quizz.data.remote.api.QuizAPI
import com.sagar.quizz.data.remote.api.UserAPI
import com.sagar.quizz.utill.Constants
import com.sagar.quizz.utill.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, Constants.DATASTORE_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(Constants.DATASTORE_NAME) }
        )

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): QuizzDatabase =
        Room.databaseBuilder(
            context = context,
            klass = QuizzDatabase::class.java,
            name = DATABASE_NAME
        ).build()

    @Provides
    fun providesDao(roomDatabase: QuizzDatabase): QuizzDao = roomDatabase.getDao()

    @Provides
    @Singleton
    fun providesRetrofitBuilder(): Builder {
        return Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesUserAPI(retrofitBuilder: Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesQuizAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): QuizAPI {
        return retrofitBuilder.client(okHttpClient).build().create(QuizAPI::class.java)
    }
}