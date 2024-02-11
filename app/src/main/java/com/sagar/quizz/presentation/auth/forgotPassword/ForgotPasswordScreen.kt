package com.sagar.quizz.presentation.auth.forgotPassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sagar.quizz.R
import com.sagar.quizz.presentation.auth.register.RegisterEvent
import com.sagar.quizz.presentation.components.ButtonWithText
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.TextFieldComponent
import com.sagar.quizz.presentation.components.TimeRemainingAndResendOTP

@Composable
fun ForgotPasswordScreen(
    //navController: NavHostController
) {

    val viewModel: ForgotPasswordViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.forgot_password),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldComponent(
                placeholder = "Enter your email",
                contentDescription = "email",
                keyboardType = KeyboardType.Email,
                value = state.email,
                onValueChange = { viewModel.onEvent(event = ForgotPasswordEvent.EmailChanged(email = it)) },
                onChangePasswordVisibility = { /*TODO*/ },
                leadingIcon = Icons.Rounded.Email,
                isError = state.emailError != null,
                errorMessage = state.emailError,
                imeAction = ImeAction.Done,
                readOnly = state.isOtpVerified
            )

            AnimatedVisibility(
                visible = state.isOtpViewVisible,
                enter = slideInVertically { with(density) { -40.dp.roundToPx() } }
                        + expandVertically(expandFrom = Alignment.Top)
                        + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column {

                    TextFieldComponent(
                        placeholder = "Enter 4 digit OTP",
                        contentDescription = "OTP",
                        keyboardType = KeyboardType.Phone,
                        value = state.otp,
                        onValueChange = {
                            viewModel.onEvent(event = ForgotPasswordEvent.OtpChanged(otp = it))
                        },
                        onChangePasswordVisibility = { /*TODO*/ },
                        leadingIcon = Icons.Default.Code,
                        imeAction = ImeAction.Next,
                        isError = state.otpError != null,
                        errorMessage = state.otpError,
                        readOnly = state.isOtpVerified
                    )

                    TimeRemainingAndResendOTP(
                        onResendOTP = { if (state.timeLeft != 0L) viewModel.onEvent(event = ForgotPasswordEvent.ResendOTP) },
                        timeRemaining = state.otpTimeRemaining
                    )

                    TextFieldComponent(
                        placeholder = "Enter new password",
                        contentDescription = "password",
                        keyboardType = KeyboardType.Password,
                        value = state.password,
                        onValueChange = {
                            viewModel.onEvent(
                                event = ForgotPasswordEvent.PasswordChanged(password = it)
                            )
                        },
                        onChangePasswordVisibility = {
                            viewModel.onEvent(
                                event = ForgotPasswordEvent.PasswordVisibilityChanged(
                                    !(state.isPasswordVisible)
                                )
                            )
                        },
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (state.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        imeAction = ImeAction.Next,
                        isError = state.passwordError != null,
                        errorMessage = state.passwordError
                    )

                    TextFieldComponent(
                        placeholder = "Confirm password",
                        contentDescription = "Confirm password",
                        keyboardType = KeyboardType.Password,
                        value = state.confirmPassword,
                        onValueChange = {
                            viewModel.onEvent(
                                event = ForgotPasswordEvent.ConfirmPasswordChanged(
                                    confirmPassword = it
                                )
                            )
                        },
                        onChangePasswordVisibility = {
                            viewModel.onEvent(
                                event = ForgotPasswordEvent.PasswordVisibilityChanged(
                                    !(state.isPasswordVisible)
                                )
                            )
                        },
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (state.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        imeAction = ImeAction.Done,
                        isError = state.confirmPasswordError != null,
                        errorMessage = state.confirmPasswordError
                    )
                }
            }

            HeightSpacer(value = 10)

            ButtonWithText(
                text = state.submitButtonText,
                onClick = { viewModel.onEvent(event = ForgotPasswordEvent.Submit) }
            )
        }
    }
}