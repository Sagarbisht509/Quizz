package com.sagar.quizz.presentation.quiz.quizResult

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.WidthSpacer
import com.sagar.quizz.presentation.quiz.playQuiz.PlayQuizViewModel
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun QuizResultScreen(
    viewModel: PlayQuizViewModel,
    onNavigateToHomeScreen: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    BackHandler { onNavigateToHomeScreen() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Quiz Result",
            fontFamily = Quicksand,
            fontSize = 35.sp,
            fontWeight = FontWeight.Medium
        )


        Text(
            text = "Congratulations!",
            fontFamily = Quicksand,
            fontSize = 26.sp,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "YOUR SCORE",
                fontFamily = Quicksand,
                fontSize = 20.sp,
            )
            HeightSpacer(value = 15)
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Green, fontSize = 26.sp)) {
                        append("${uiState.correctAnswers} ")
                    }
                    append("/ ${uiState.questionCount}")
                },
                fontFamily = Quicksand,
                fontSize = 24.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EXP EARNED",
                fontFamily = Quicksand,
                fontSize = 20.sp,
            )
            HeightSpacer(value = 15)
            Text(
                text = "${uiState.correctAnswers * 2}",
                fontFamily = Quicksand,
                fontSize = 24.sp,
                color = Color.Green
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavigationItem(
                color = Color.Yellow,
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = "go to home",
                text = "Home",
                enable = true,
                onClick = onNavigateToHomeScreen
            )

            NavigationItem(
                color = Color.Blue,
                painter = painterResource(id = R.drawable.ic_upvote),
                contentDescription = "upvote quiz",
                text = "Upvote",
                enable = uiState.upVoted,
                onClick = { viewModel.onUpVoteButtonClicked() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { },
                border = BorderStroke(1.dp, color = colorResource(id = R.color.green)),
                modifier = Modifier.weight(.5f)
            ) {
                Text(
                    text = stringResource(R.string.share_result),
                    color = colorResource(id = R.color.green),
                    fontFamily = Quicksand,
                    fontSize = 16.sp
                )
            }
            WidthSpacer(value = 10)
            Button(
                onClick = { onNavigateToHomeScreen() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green)
                ),
                modifier = Modifier.weight(.5f)
            ) {
                Text(
                    text = stringResource(R.string.take_new_quiz),
                    fontFamily = Quicksand,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    color: Color,
    painter: Painter,
    contentDescription: String,
    text: String,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.background),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(15.dp)
                .clickable(
                    onClick = onClick,
                    enabled = enable
                )
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
            )
        }
        HeightSpacer(value = 5)
        Text(
            text = text,
            fontFamily = Quicksand
        )
    }
}


// ******************** Preview *******************

@Preview(showSystemUi = true)
@Composable
fun PreviewResultScreen() {
    QuizResultScreen(
        viewModel = hiltViewModel(),
        onNavigateToHomeScreen = {}
    )
}