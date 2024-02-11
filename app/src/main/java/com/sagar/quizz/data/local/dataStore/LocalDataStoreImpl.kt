package com.sagar.quizz.data.local.dataStore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.sagar.quizz.data.model.remote.auth.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalDataStore {

    companion object {
        val userIdKey = stringPreferencesKey(name = "user_id_pref")
        val userKey = stringPreferencesKey(name = "user_data_pref")
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_pref")
        val loginKey = booleanPreferencesKey(name = "login_pref")
        val tokenKey = stringPreferencesKey(name = "token_pref")
    }

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[onBoardingKey] = completed
        }
    }

    override suspend fun readOnBoardingState(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[onBoardingKey] ?: false
        }.first()
    }

    override suspend fun saveLoginState(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[loginKey] = isLogin
        }
    }

    override suspend fun readLoginState(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[loginKey] ?: false
        }.first()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    override fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    override suspend fun removeToken() {
        dataStore.edit { preference -> preference.remove(key = tokenKey) }
    }

    override suspend fun saveUserData(user: User) {
        dataStore.edit { preference ->
            preference[userKey] = Gson().toJson(user)
        }
    }

    override suspend fun removeUserData() {
        dataStore.edit { preference -> preference.remove(userKey) }
    }

    override fun getUserDataFlow(): Flow<User> {
        return dataStore.data.map { preference ->
            Gson().fromJson(preference[userKey], User::class.java)
        }
    }

    override suspend fun getUserData(): User = getUserDataFlow().first()

    override suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences -> preferences[userIdKey] = userId }
    }

    override fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[userIdKey] ?: ""
        }
    }

    override suspend fun removeUserId() {
        dataStore.edit { preference -> preference.remove(userIdKey) }
    }
}