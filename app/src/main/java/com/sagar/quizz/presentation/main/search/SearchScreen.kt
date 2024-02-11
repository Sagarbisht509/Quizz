package com.sagar.quizz.presentation.main.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Category
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.QuizItem
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    onNavigateToQuizDetailScreen: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val quizzes by viewModel.quizzes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(16.dp)
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(query = it) }
        )
        HeightSpacer(value = 4)
        CategoryFilter(
            categoryList = uiState.categories,
            onCategoryClicked = { index -> viewModel.onCategoryChanged(index = index) }
        )

        if (quizzes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_search_state),
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.ExtraLight,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            QuizList(
                quizzes = quizzes,
                onQuizSelected = { quiz -> onNavigateToQuizDetailScreen(quiz) }
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth(1f),
        shape = RoundedCornerShape(30.dp),
        placeholder = {
            Text(text = "Search quiz here", fontFamily = Quicksand)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear",
                        tint = Color.Black
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = colorResource(id = R.color.background),
            focusedContainerColor = colorResource(id = R.color.background),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}

@Composable
fun CategoryFilter(
    categoryList: List<Category>,
    onCategoryClicked: (Int) -> Unit
) {
    LazyRow {
        categoryList.forEachIndexed { index, category ->
            item {
                FilterChipItem(
                    category = category,
                    onCategoryClicked = { onCategoryClicked(index) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipItem(
    category: Category,
    onCategoryClicked: () -> Unit
) {
    FilterChip(
        selected = category.isSelected,
        onClick = { onCategoryClicked() },
        label = { Text(text = category.name, fontFamily = Quicksand) },
        modifier = Modifier.padding(4.dp),
        shape = CircleShape
    )
}

@Composable
fun QuizList(
    quizzes: List<Quiz>,
    onQuizSelected: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = quizzes,
            key = { it._id }
        ) { quiz ->
            QuizItem(
                quiz = quiz,
                onQuizSelected = { onQuizSelected(quiz._id) },
                onQuizLongPressed = {}
            )
        }
    }
}

// ***********************************


@Preview(showSystemUi = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen(
        viewModel = hiltViewModel(),
        onNavigateToQuizDetailScreen = {}
    )
}
