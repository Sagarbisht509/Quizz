package com.sagar.quizz.presentation.quiz.quizDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.ButtonWithText
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.quiz.playQuiz.PlayQuizViewModel
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun QuizDetailsScreen(
    quizId: String,
    viewModel: PlayQuizViewModel,
    onNavigateToPlayQuizScreen: () -> Unit
) {

    viewModel.setQuizId(quizId)
    viewModel.collectIsQuizUserFav()

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.playing_games))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(25.dp),
            content = {
                LottieAnimation(
                    composition = composition,
                    progress = { progress }
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.background),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.padding(top = 10.dp)) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            ),
                    ) {
                        Icon(
                            imageVector = if (uiState.isFavourite) Icons.Rounded.Bookmark
                                         else Icons.Rounded.BookmarkBorder,
                            contentDescription = "Fav Quiz",
                            modifier = Modifier
                                .padding(12.dp)
                                .clickable(
                                    onClick = { viewModel.onFavouriteIconClicked() },
                                    enabled = !uiState.isLoading
                                ),
                            tint = Color.Black
                        )
                    }
                }

                HeightSpacer(value = 15)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = uiState.quiz.title,
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.join),
                        contentDescription = "Quiz Image",
                        modifier = Modifier
                            .size(80.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp)
                    )
                }

                HeightSpacer(value = 10)

                Text(
                    text = "Questions: ${uiState.questionCount}",
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Medium
                )
                HeightSpacer(value = 6)
                Text(
                    text = "Category: ${uiState.quiz.category}",
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Medium
                )
            }

            ButtonWithText(
                text = stringResource(R.string.start_quiz),
                onClick = onNavigateToPlayQuizScreen
            )
            HeightSpacer(value = 15)
        }
    }

}

// *************************


/*@Preview(showSystemUi = true)
@Composable
fun PreviewQuizDetails() {
    QuizDetailsScreen(
        quizId = "",
        viewModel = hiltViewModel() ,
        onNavigateToPlayQuizScreen = {}
    )
}*/
