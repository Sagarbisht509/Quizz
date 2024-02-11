package com.sagar.quizz.presentation.quiz.createQuiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sagar.quizz.R
import com.sagar.quizz.data.model.remote.quiz.Category
import com.sagar.quizz.data.model.remote.quiz.Question
import com.sagar.quizz.presentation.components.ErrorText
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.WidthSpacer
import com.sagar.quizz.ui.theme.Quicksand
import com.sagar.quizz.utill.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    viewModel: CreateQuizViewModel,
    onNavigateToHomeScreen: () -> Unit
) {

    val quizState by viewModel.quizUiState.collectAsState()
    val questionState by viewModel.questionState.collectAsState()

    LaunchedEffect(key1 = quizState.quizCreated) {
        if (quizState.quizCreated) {
            onNavigateToHomeScreen()
        }
    }

    val categories = Constants.categoryList

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                isLoading = quizState.isLoading,
                onClick = { viewModel.onQuizEvent(CreateQuizEvent.CreateQuiz) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = Color.White
    ) { paddingValue ->
        Box(modifier = Modifier.padding(paddingValue)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                HeightSpacer(value = 16)

                TextField(
                    value = quizState.title,
                    onValueChange = { viewModel.onQuizEvent(CreateQuizEvent.TitleChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .border(
                            width = 1.dp,
                            color = if (quizState.titleError != null) Color.Red else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_quiz_title),
                            fontFamily = Quicksand,
                            color = Color.Black
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLines = 1,
                    readOnly = quizState.isLoading,
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

                ErrorText(errorMessage = quizState.titleError)

                HeightSpacer(value = 10)

                CategoryDropDown(
                    categories = categories,
                    expanded = quizState.isExpended,
                    selectedCategory = quizState.category,
                    onExpandedChange = {
                        viewModel.onQuizEvent(CreateQuizEvent.Expended(!(quizState.isExpended)))
                    },
                    onCategorySelected = { category ->
                        viewModel.onQuizEvent(CreateQuizEvent.CategoryChanged(category))
                    },
                    isError = quizState.categoryError != null
                )

                ErrorText(errorMessage = quizState.categoryError)

                HeightSpacer(value = 15)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.questions),
                        fontFamily = Quicksand,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = {
                            viewModel.onQuizEvent(
                                event = CreateQuizEvent.BottomSheetStateChanged(isOpened = true)
                            )
                        },
                        enabled = !quizState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_question),
                            tint = colorResource(id = R.color.green)
                        )
                    }
                }

                QuestionsSection(
                    scope = scope,
                    snackBarHostState = snackBarHostState,
                    questions = quizState.questions,
                    onRemoveQuestion = { question ->
                        viewModel.onQuizEvent(event = CreateQuizEvent.RemoveQuestion(question = question))
                        viewModel.onQuizEvent(event = CreateQuizEvent.HoldRemovedQuestion(question = question))
                    },
                    undoRemovedQuestion = {
                        viewModel.onQuizEvent(event = CreateQuizEvent.UndoRemovedQuestion(question = quizState.recentRemovedQuestion!!))
                    }
                )
            }
        }
    }

    if (quizState.showBottomSheet) {
        BottomSheet(
            questionState = questionState,
            viewModel = viewModel,
            onDismiss = {
                viewModel.onQuestionEvent(event = QuestionEvent.DiscardQuestion)
                viewModel.onQuizEvent(event = CreateQuizEvent.BottomSheetStateChanged(isOpened = false))
            },
            onSave = {
                viewModel.onQuestionEvent(event = QuestionEvent.SaveQuestion)
                viewModel.onQuizEvent(event = CreateQuizEvent.BottomSheetStateChanged(isOpened = false))
            }
        )
    }
}

@Composable
fun QuestionsSection(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    questions: List<Question>,
    onRemoveQuestion: (Question) -> Unit,
    undoRemovedQuestion: () -> Unit
) {
    LazyColumn {
        items(questions) { question ->
            QuestionItem(
                question = question,
                onDeleteQuestion = {
                    onRemoveQuestion(question)
                    scope.launch {
                        val result = snackBarHostState
                            .showSnackbar(
                                message = "Question removed",
                                actionLabel = "UNDO",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                undoRemovedQuestion()
                            }

                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FloatingActionButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = {
            val text = if (isLoading) "Loading..." else stringResource(R.string.upload_quiz)
            Text(text = text, fontFamily = Quicksand)
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Upload,
                contentDescription = stringResource(R.string.upload_quiz)
            )
        },
        onClick = onClick,
        containerColor = colorResource(id = R.color.green),
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(
            defaultElevation = 20.dp,
            pressedElevation = 30.dp
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    categories: List<Category>,
    expanded: Boolean,
    selectedCategory: String,
    onExpandedChange: () -> Unit,
    onCategorySelected: (String) -> Unit,
    isError: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isError) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            TextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.select_category),
                        fontFamily = Quicksand,
                        color = Color.Black
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(1f),
                shape = RoundedCornerShape(12.dp),
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

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange() }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            onCategorySelected(item.name)
                            //  onExpandedChange()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    questionState: QuestionState,
    viewModel: CreateQuizViewModel,
    radioOptions: List<String> = listOf("Option 1", "Option 2", "Option 3", "option 4"),
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            McqTextField(
                label = stringResource(R.string.write_question),
                value = questionState.question,
                onValueChange = { viewModel.onQuestionEvent(QuestionEvent.QuestionChanged(it)) },
                isError = questionState.isQuestionEmpty
            )

            HeightSpacer(value = 6)

            radioOptions.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = index + 1 == questionState.answer,
                        onClick = {
                            viewModel.onQuestionEvent(QuestionEvent.AnswerChanged(index + 1))
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.green)
                        )
                    )

                    McqTextField(
                        label = option,
                        value = questionState.options[index],
                        onValueChange = {
                            viewModel.onQuestionEvent(
                                event = QuestionEvent.OptionChanged(index = index, option = it)
                            )
                        },
                        isError = questionState.isOptionsEmpty[index]
                    )
                }
            }

            HeightSpacer(value = 12)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.green)),
                    modifier = Modifier.weight(.5f)
                ) {
                    Text(
                        text = stringResource(R.string.discard),
                        color = colorResource(id = R.color.green),
                        fontFamily = Quicksand,
                        fontSize = 16.sp
                    )
                }
                WidthSpacer(value = 10)
                Button(
                    onClick = onSave,
                    enabled =  questionState.showSaveButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.green)
                    ),
                    modifier = Modifier.weight(.5f)
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McqTextField(
    label: String,
    value: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        isError = isError,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedBorderColor = colorResource(id = R.color.green),
            focusedLabelColor = colorResource(id = R.color.green),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}

// **************** Preview ****************

@Preview(showSystemUi = true)
@Composable
fun PreviewCreateQuizScreen() {
    CreateQuizScreen(
        viewModel = hiltViewModel(),
        onNavigateToHomeScreen = {}
    )
}