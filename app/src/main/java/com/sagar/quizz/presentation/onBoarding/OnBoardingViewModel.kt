package com.sagar.quizz.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.local.dataStore.LocalDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
     private val dataStore: LocalDataStore
): ViewModel() {

    fun saveOnBoardingState(completed : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveOnBoardingState(completed)
        }
    }
}