package com.sagar.quizz.presentation.main.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagar.quizz.data.repo.quiz.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val quizRepo: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState = _uiState.asStateFlow()

    private val selectedCategories = MutableStateFlow<List<String>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val quizzes = searchQuery
        .combine(selectedCategories) { query, category ->
            Pair(query, category)
        }
        .flatMapLatest {
            quizRepo.getAllQuizzes(it.first, it.second)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) = viewModelScope.launch {
        _searchQuery.emit(value = query)
    }

    fun onCategoryChanged(index: Int) = viewModelScope.launch {

        if (uiState.value.categories[index].isSelected)
            removeCategory(uiState.value.categories[index].name)
        else
            addCategory(uiState.value.categories[index].name)

        _uiState.update { currentState ->
            val updatedCategories = currentState.categories.toMutableList()
            updatedCategories[index] =
                updatedCategories[index].copy(isSelected = !updatedCategories[index].isSelected)
            currentState.copy(categories = updatedCategories)
        }
    }

    // Function to add a new category
    private fun addCategory(category: String) {
        selectedCategories.value = selectedCategories.value + category
    }

    // Function to remove a category
    private fun removeCategory(category: String) {
        selectedCategories.value = selectedCategories.value - category
    }
}