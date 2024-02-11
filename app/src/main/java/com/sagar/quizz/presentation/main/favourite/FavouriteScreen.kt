package com.sagar.quizz.presentation.main.favourite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.QuizItem
import com.sagar.quizz.presentation.components.ShowAlertDialog
import com.sagar.quizz.presentation.components.SwipeToDelete
import com.sagar.quizz.presentation.components.showToast
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun FavouriteScreen(
    viewModel: FavouriteViewModel,
    onNavigateToQuizDetailScreen: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.message) {
        if (uiState.message != null) {
            showToast(context = context, message = uiState.message!!)
        }
    }

    if (uiState.isEmptyStateVisible || uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading)
                CircularProgressIndicator(color = Color.Green)
            else
                Text(
                    text = stringResource(R.string.empty_favourites_state),
                    fontFamily = Quicksand,
                    textAlign = TextAlign.Center
                )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.favourite),
                fontFamily = Quicksand,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )

            HeightSpacer(value = 15)

            LazyColumn {
                items(
                    items = uiState.quizzes,
                    key = { it._id }
                ) { quiz ->
                    SwipeToDelete(
                        item = quiz,
                        onDelete = { viewModel.quizIdChanged(id = quiz._id) }
                    ) { quiz ->
                        QuizItem(
                            quiz = quiz,
                            onQuizSelected = { onNavigateToQuizDetailScreen(quiz._id) },
                            onQuizLongPressed = { viewModel.quizIdChanged(id = quiz._id) }
                        )
                    }
                }
            }
        }
    }

    if (uiState.quizId != null) {
        ShowAlertDialog(
            onDismissRequest = { viewModel.quizIdChanged(id = null) },
            onConfirmation = {
                viewModel.onRemoveQuiz(quizId = uiState.quizId!!)
            },
            dialogTitle = stringResource(R.string.remove_from_favorites),
            dialogText = stringResource(R.string.remove_from_fav_text)
        )
    }
}

// ******************** Preview *******************
/*
@Preview(showBackground = true)
@Composable
fun PreviewFav() {
    FavouriteScreen()
}*/
