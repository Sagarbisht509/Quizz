package com.sagar.quizz.presentation.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.rounded.ArrowDownward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.IconWithCircularBackground
import com.sagar.quizz.presentation.components.MultiStyleText
import com.sagar.quizz.presentation.components.TextFieldComponent
import com.sagar.quizz.presentation.components.showToast

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = state.successfullyRegistered) {
        if (state.successfullyRegistered) {
            onNavigateToHome()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = state.toastMessage) {
        if (state.toastMessage != null) {
            showToast(context, state.toastMessage!!)
        }
    }

    AnimatedVisibility(
        visible = !state.showAvatarContent,
        exit = fadeOut() + shrinkVertically()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.register),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = colorResource(id = R.color.green),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            HeightSpacer(value = 6)
            Text(
                text = stringResource(id = R.string.create_your_new_account),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
            HeightSpacer(value = 30)
            TextFieldComponent(
                placeholder = stringResource(R.string.full_name),
                contentDescription = "Full Name",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text,
                value = state.name,
                onValueChange = { name ->
                    viewModel.onEvent(RegisterEvent.NameChanged(name = name))
                },
                onChangePasswordVisibility = {},
                imeAction = ImeAction.Next,
                isError = state.nameError != null,
                errorMessage = state.nameError,
            )
            HeightSpacer(value = 8)
            TextFieldComponent(
                placeholder = stringResource(R.string.user_gmail_com),
                contentDescription = stringResource(R.string.email),
                leadingIcon = Icons.Rounded.Email,
                trailingIcon = if (state.emailError == null && state.email.isNotEmpty()) Icons.Default.Verified else null,
                keyboardType = KeyboardType.Email,
                value = state.email,
                onValueChange = { email ->
                    viewModel.onEvent(RegisterEvent.EmailChanged(email = email))
                },
                onChangePasswordVisibility = {},
                imeAction = ImeAction.Next,
                isError = state.emailError != null,
                errorMessage = state.emailError
            )
            HeightSpacer(value = 8)
            TextFieldComponent(
                placeholder = "*******",
                contentDescription = "password",
                leadingIcon = Icons.Rounded.Lock,
                trailingIcon = if (state.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                keyboardType = KeyboardType.Password,
                value = state.password,
                onValueChange = { password ->
                    viewModel.onEvent(RegisterEvent.PasswordChanged(password = password))
                },
                onChangePasswordVisibility = {
                    viewModel.onEvent(RegisterEvent.PasswordVisibilityChanged(isPasswordVisible = !(state.isPasswordVisible)))
                },
                imeAction = ImeAction.Done,
                isError = state.passwordError != null,
                errorMessage = state.passwordError,
                isVisible = state.isPasswordVisible
            )

            HeightSpacer(value = 4)

            MultiStyleText(
                text1 = stringResource(R.string.already_have_an_account),
                color1 = Color.Gray,
                text2 = stringResource(R.string.sign_in),
                color2 = colorResource(id = R.color.green),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        onClick = onNavigateToSignIn,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            HeightSpacer(value = 15)

            AnimatedVisibility(visible = state.isNextButtonVisible) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconWithCircularBackground(
                        icon = Icons.Rounded.ArrowDownward,
                        backgroundColor = colorResource(id = R.color.green),
                        onClick = { viewModel.onEvent(event = RegisterEvent.NextButtonClicked) }
                    )
                }
            }
        }

    }

    AnimatedVisibility(
        visible = state.showAvatarContent,
        enter = fadeIn() + expandVertically()
    ) {
        AvatarContent(
            avatars = state.avatars,
            selectedAvatarIndex = state.selectedAvatarIndex,
            isLoading = state.isLoading,
            onAvatarChanged = { index ->
                viewModel.onEvent(event = RegisterEvent.AvatarChanged(index = index))
            },
            registerButtonClicked = {
                viewModel.onEvent(event = RegisterEvent.RegisterButtonClicked)
            }
        )
    }

}

// ************ preview ************

/*
@Preview(name = "Register screen", showSystemUi = true)
@Preview(
    name = "Register screen (dark)",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Register (big font)", showSystemUi = true, fontScale = 1.5f)
@Composable
fun PreviewRegisterScreen() {
    //  RegisterScreen()
}*/


/*
TextFieldComponent(
                placeholder = "Enter 4 digit OTP",
                contentDescription = "OTP",
                keyboardType = KeyboardType.Phone,
                value = state.otp,
                onValueChange = { viewModel.onEvent(RegisterEvent.OtpChanged(otp = it)) },
                onChangePasswordVisibility = { /*TODO*/ },
                leadingIcon = Icons.Default.Code,
                imeAction = ImeAction.Next,
                isError = state.otpError != null,
                errorMessage = state.otpError
            )

            TimeRemainingAndResendOTP(
                onResendOTP = { if (state.timeLeft != 0L) viewModel.onEvent(event = RegisterEvent.ResendOTP) },
                timeRemaining = state.otpTimeRemaining
            )
 */