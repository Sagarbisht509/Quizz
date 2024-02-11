package com.sagar.quizz.presentation.quiz.createQuiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Question
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.ui.theme.Quicksand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionItem(
    question: Question,
    onDeleteQuestion: () -> Unit
) {
    Card(
        onClick = {},
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.background)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.question,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Quicksand,
                    modifier = Modifier.fillMaxWidth(.9f),
                )

                IconButton(onClick = { onDeleteQuestion() }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete Question")
                }
            }

            question.options.forEachIndexed { index, option ->
                OptionItem(optionNumber = "Option ${index + 1} :", text = option)
            }

            HeightSpacer(value = 5)

            Text(text = "Answer: ${question.answer}", fontFamily = Quicksand)
        }
    }
}

@Composable
fun OptionItem(
    optionNumber: String,
    text: String
) {
    HeightSpacer(value = 5)
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = optionNumber, fontFamily = Quicksand)

        Text(text = text, fontFamily = Quicksand)
    }
}

// ******************** PREVIEW ************************

/*
@Preview(showBackground = true)
@Composable
fun PreviewQuestionItem() {
    QuestionItem(
        Question(
            question = "When was kotlin released?",
            options = listOf("2007", "2010", "2012", "2016"),
            answer = 1
        ),
        onDeleteQuestion = {}
    )
}*/
