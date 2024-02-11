package com.sagar.quizz.presentation.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.local.dataStore.LocalDataStore
import com.sagar.quizz.presentation.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val dataStore: LocalDataStore
): ViewModel() {

    private val _startDestination = mutableStateOf(Destination.OnBoarding.route)
    val startDestination: State<String> = _startDestination

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init { setInitialScreen() }

    private suspend fun isOnBoardingComplete() = dataStore.readOnBoardingState()

    private suspend fun  isUserLoggedIn() = dataStore.readLoginState()

    private fun setInitialScreen() = viewModelScope.launch(Dispatchers.IO) {
        _startDestination.value = when {
            !isOnBoardingComplete() -> Destination.OnBoarding.route
            !isUserLoggedIn() -> Destination.Auth.route
            else -> Destination.Main.route
        }

        _isLoading.value = false
    }
}