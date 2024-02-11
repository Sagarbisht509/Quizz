package com.sagar.quizz.presentation.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Quiz
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.QuizItem
import com.sagar.quizz.presentation.components.TextFieldComponent
import com.sagar.quizz.presentation.components.showToast
import com.sagar.quizz.ui.theme.Quicksand

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCreateQuizScreen: () -> Unit,
    onNavigateToQuizDetailScreen: (String) -> Unit
) {

    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = state.quizId) {
        if (state.quizId != "") {
            viewModel.removeDataFromState()
            onNavigateToQuizDetailScreen(state.quizId)
        }
    }

    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage != null) {
            showToast(context = context, message = state.errorMessage!!)
        }
    }

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { viewModel.onRefreshed() })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.green),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.hello_again),
                    color = Color.White,
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                HeightSpacer(value = 6)
                Text(
                    text = stringResource(R.string.welcome_back_you_ve_been_missed),
                    color = Color.White,
                    fontFamily = Quicksand,
                    modifier = Modifier.padding(start = 16.dp, bottom = 20.dp)
                )
            }
            HeightSpacer(value = 25)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeScreenCard(
                    title = stringResource(R.string.create_new_quiz),
                    image = painterResource(id = R.drawable.create),
                    onClick = { onNavigateToCreateQuizScreen() }
                )
                HomeScreenCard(
                    title = stringResource(R.string.join_quiz),
                    image = painterResource(id = R.drawable.join),
                    onClick = { viewModel.onEvent(event = HomeScreenEvent.QuizCodeStateChanged(true)) }
                )
            }
            HeightSpacer(value = 20)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.top_rated),
                    fontFamily = Quicksand,
                    fontSize = 16.sp
                )

                Text(
                    text = stringResource(R.string.all),
                    fontFamily = Quicksand,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { /*navController.navigate(route = Destination.Quiz.route)*/ }
                )
            }
            HeightSpacer(value = 10)

            if (state.isLoading) {
                //CircularProgressIndicator()
            } else {
                QuizList(
                    state.topRatedQuiz,
                    onQuizSelected = { quizId ->
                        onNavigateToQuizDetailScreen(quizId)
                        viewModel.removeDataFromState()
                    }
                )
            }
        }

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    when {
        state.openAlertDialog -> {
            ShowAlertDialog(
                onDismissRequest = {
                    viewModel.onEvent(event = HomeScreenEvent.QuizCodeChanged(""))
                    viewModel.onEvent(event = HomeScreenEvent.QuizCodeStateChanged(value = false))
                },
                onConfirmation = {
                    viewModel.onEvent(event = HomeScreenEvent.JoinQuizButtonClicked)
                    viewModel.onEvent(event = HomeScreenEvent.QuizCodeStateChanged(value = false))
                },
                dialogTitle = stringResource(R.string.join_quiz),
                value = state.quizCode,
                onValueChange = { code ->
                    viewModel.onEvent(event = HomeScreenEvent.QuizCodeChanged(code))
                },
                showConformationButton = state.showConformationButton,
                isError = state.quizCodeError
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeScreenCard(
    title: String,
    image: Painter,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.size(width = 150.dp, height = 100.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            HeightSpacer(value = 6)
            Text(
                text = title,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun QuizList(
    quizzes: List<Quiz>,
    onQuizSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
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


@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    value: String,
    onValueChange: (String) -> Unit,
    showConformationButton: Boolean,
    isError: Boolean
) {

    AlertDialog(
        title = { Text(text = dialogTitle, fontFamily = Quicksand) },
        text = {
                TextFieldComponent(
                    placeholder = stringResource(R.string.enter_4_digit_code),
                    contentDescription = stringResource(R.string.join_quiz_with_code),
                    keyboardType = KeyboardType.Number,
                    leadingIcon = Icons.Default.Pin,
                    value = value,
                    onValueChange = { onValueChange(it) },
                    onChangePasswordVisibility = {},
                    isError = isError
                )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation, enabled = showConformationButton) {
                Text(
                    text = stringResource(R.string.confirm),
                    fontFamily = Quicksand
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontFamily = Quicksand
                )
            }
        }
    )
}
