package com.sagar.quizz.presentation.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.ButtonWithText
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.MultiStyleText
import com.sagar.quizz.presentation.components.ShowAnimatedLoading
import com.sagar.quizz.presentation.components.TextFieldComponent
import com.sagar.quizz.presentation.components.showToast
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSignup: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPasswordScreen: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = state.successfullyLoggedIn) {
        if (state.successfullyLoggedIn) {
            onNavigateToHome()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = state.toastMessage) {
        if (state.toastMessage != null) {
            showToast(context, state.toastMessage!!)
        }
    }

    Column(
        modifier = Modifier
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.Crop
            )
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.welcome_back),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = colorResource(id = R.color.green),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = Quicksand
        )
        HeightSpacer(value = 6)
        Text(
            text = stringResource(R.string.login_to_your_account),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Gray,
            fontFamily = Quicksand
        )
        HeightSpacer(value = 30)
        TextFieldComponent(
            placeholder = stringResource(R.string.user_gmail_com),
            contentDescription = stringResource(R.string.email),
            leadingIcon = Icons.Rounded.Email,
            trailingIcon = if (state.emailError == null && state.email.isNotEmpty()) Icons.Default.Verified else null,
            keyboardType = KeyboardType.Email,
            value = state.email,
            onValueChange = { userInput ->
                viewModel.onEvent(LoginEvent.EmailChanged(userInput))
            },
            onChangePasswordVisibility = {},
            imeAction = ImeAction.Next,
            isError = state.emailError != null,
            errorMessage = state.emailError,
            readOnly = state.isLoading
        )
        HeightSpacer(value = 8)
        TextFieldComponent(
            placeholder = "*******",
            contentDescription = "password",
            leadingIcon = Icons.Rounded.Lock,
            trailingIcon = if (state.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
            keyboardType = KeyboardType.Password,
            value = state.password,
            onValueChange = { userInput ->
                viewModel.onEvent(LoginEvent.PasswordChanged(userInput))
            },
            onChangePasswordVisibility = {
                viewModel.onEvent(LoginEvent.PasswordVisibilityChanged(!(state.isPasswordVisible)))
            },
            imeAction = ImeAction.Done,
            isError = state.passwordError != null,
            errorMessage = state.passwordError,
            isVisible = state.isPasswordVisible,
            readOnly = state.isLoading
        )
        HeightSpacer(value = 5)
        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(end = 10.dp)
                .clickable(
                    onClick = onNavigateToForgotPasswordScreen,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            color = colorResource(id = R.color.green),
            fontFamily = Quicksand
        )
        HeightSpacer(value = 20)
        ButtonWithText(
            text = stringResource(R.string.login),
            modifier = Modifier.padding(5.dp),
            enabled = !state.isLoading,
            onClick = { viewModel.onEvent(event = LoginEvent.LoginButtonClicked) }
        )
        HeightSpacer(value = 4)

        MultiStyleText(
            text1 = stringResource(R.string.don_t_have_account),
            color1 = Color.Gray,
            text2 = stringResource(R.string.sign_up),
            color2 = colorResource(id = R.color.green),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(
                    onClick = onNavigateToSignup,
                    enabled = !state.isLoading,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        ShowAnimatedLoading(topPadding = 15, visibility = state.isLoading)
    }
}

// ************ preview ************

/*
@Preview("Login screen", showSystemUi = true)
@Preview("Login screen (dark)", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Login (big font)", showSystemUi = true, fontScale = 1.5f)
@Composable
fun PreviewLoginScreen() {
    //LoginScreen()
}
*/
