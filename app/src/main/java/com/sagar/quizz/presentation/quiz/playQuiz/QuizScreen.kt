package com.sagar.quizz.presentation.quiz.playQuiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.WidthSpacer
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun QuizScreen(
    viewModel: PlayQuizViewModel,
    onNavigateToResultScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    if (state.isQuizFinished) {
        onNavigateToResultScreen()
    }
    
    BackHandler {
        onNavigateToHomeScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.quiz, state.quiz.category),
                    fontFamily = Quicksand,
                    fontSize = 16.sp
                )
                TextButton(onClick = { onNavigateToHomeScreen() }) {
                    Text(
                        text = stringResource(R.string.leave),
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            HeightSpacer(value = 10)

            Text(
                text = state.questionCountText,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Medium
            )
            LinearProgressIndicator(
                progress = state.progress,
                color = colorResource(id = R.color.green),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .size(20.dp)
                    .clip(RoundedCornerShape(percent = 50))
            )
            Text(
                text = state.currentQuestionTimeLeft,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                fontFamily = Quicksand,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            HeightSpacer(value = 15)

            Text(
                text = state.question,
                fontSize = 20.sp,
                fontFamily = Quicksand,
            )

            HeightSpacer(value = 10)

            state.options.forEachIndexed { index, option ->
                HeightSpacer(value = 12)
                OptionItem2(
                    correctOption = state.answer,
                    option = option,
                    currentIndex = index,
                    showResult = state.isResultsShowing,
                    selectedIndex = state.currentOptionSelected,
                    onOptionSelection = {
                        viewModel.onEvent(
                            event = PlayQuizEvent.OnOptionSelected(selectedOption = index + 1)
                        )
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { viewModel.onEvent(event = PlayQuizEvent.SkipButtonClicked) },
                border = BorderStroke(1.dp, color = colorResource(id = R.color.green)),
                modifier = Modifier.weight(.5f),
                enabled = !state.isResultsShowing
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    color = colorResource(id = R.color.green),
                    fontFamily = Quicksand,
                    fontSize = 16.sp
                )
            }
            WidthSpacer(value = 10)
            Button(
                onClick = { viewModel.onEvent(event = PlayQuizEvent.NextButtonClicked) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green)
                ),
                modifier = Modifier.weight(.5f),
                enabled = !state.isResultsShowing
            ) {
                Text(
                    text = stringResource(R.string.next),
                    fontFamily = Quicksand,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun OptionItem2(
    correctOption: Int,
    option: String,
    currentIndex: Int,
    showResult: Boolean,
    selectedIndex: Int,
    onOptionSelection: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (showResult) {
                    if (currentIndex + 1 == correctOption || currentIndex + 1 == selectedIndex) 2.dp else 1.dp
                } else if (currentIndex + 1 == selectedIndex) 2.dp else 1.dp,
                color = if (showResult) {
                    if (currentIndex + 1 == correctOption) Color.Green
                    else {
                        if (currentIndex + 1 == selectedIndex) Color.Red else Color.Gray
                    }
                } else {
                    if (currentIndex + 1 == selectedIndex) Color.Blue else Color.Gray
                },
                shape = CircleShape
            )
            .clickable(
                enabled = !showResult,
                onClick = { onOptionSelection() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedIndex - 1 == currentIndex || (showResult && currentIndex + 1 == correctOption),
            onClick = { onOptionSelection() },
            colors = RadioButtonDefaults.colors(
                selectedColor = if (showResult) {
                    if (currentIndex + 1 == correctOption) Color.Green
                    else Color.Red
                } else if (currentIndex + 1 == selectedIndex) Color.Blue else Color.Unspecified
            )
        )

        Text(
            text = option,
            fontFamily = Quicksand,
            fontSize = 16.sp
        )
    }
}

// ******************** Preview *******************

/*
@Preview(showSystemUi = true)
@Composable
fun PreviewQuizScreen() {
    QuizScreen()
}*/
