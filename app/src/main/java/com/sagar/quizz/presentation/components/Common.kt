package com.sagar.quizz.presentation.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagar.quizz.R
import com.sagar.quizz.ui.theme.Quicksand


@Composable
fun ButtonWithText(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(
                id = R.color.green
            )
        )
    ) {
        Text(
            text = text,
            modifier = modifier,
            fontFamily = Quicksand,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    placeholder: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    contentDescription: String,
    keyboardType: KeyboardType,
    value: String,
    onValueChange: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    isError: Boolean = false,
    errorMessage: String? = null,
    isVisible: Boolean = false,
    readOnly: Boolean = false
) {
    Column {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth(1f)
                .border(
                    width = 1.dp,
                    color = if (isError) Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(text = placeholder, color = Color.Gray, fontFamily = Quicksand)
            },
            visualTransformation =
            if (keyboardType == KeyboardType.Password) {
                if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = contentDescription,
                        tint = colorResource(
                            id = R.color.green
                        )
                    )
                }
            },
            trailingIcon = {
                if (trailingIcon != null)
                    IconButton(onClick = { onChangePasswordVisibility() }) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = contentDescription,
                            tint = colorResource(
                                id = R.color.green
                            )
                        )
                    }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            //isError = isError,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = colorResource(id = R.color.background),
                focusedContainerColor = colorResource(id = R.color.background),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            readOnly = readOnly
        )

        ErrorText(errorMessage = errorMessage)
    }
}

@Composable
fun MultiStyleText(
    text1: String,
    color1: Color,
    text2: String,
    color2: Color,
    modifier: Modifier = Modifier
) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = color1, fontFamily = Quicksand)) {
                append(text1)
            }
            withStyle(
                style = SpanStyle(
                    color = color2,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Quicksand
                )
            ) {
                append(text2)
            }
        },
        modifier = Modifier.then(modifier),
        fontFamily = Quicksand
    )
}

@Composable
fun WidthSpacer(value: Int) {
    Spacer(modifier = Modifier.width(value.dp))
}

@Composable
fun HeightSpacer(value: Int) {
    Spacer(modifier = Modifier.height(value.dp))
}

@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String? = null
) {

    AlertDialog(
        title = {
            Text(text = dialogTitle, fontFamily = Quicksand)
        },
        text = {
            if (dialogText != null) {
                Text(text = dialogText, fontFamily = Quicksand)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
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

@Composable
fun ErrorText(errorMessage: String?) {
    Text(
        text = errorMessage ?: "",
        color = MaterialTheme.colorScheme.error,
        fontFamily = Quicksand,
        modifier = Modifier.padding(start = 5.dp)
    )
}

@Composable
fun TimeRemainingAndResendOTP(
    onResendOTP: () -> Unit,
    timeRemaining: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.time_remaining, timeRemaining), fontFamily = Quicksand)

        Text(
            text = stringResource(R.string.resend_otp),
            fontFamily = Quicksand,
            color = Color.Gray,
            modifier = Modifier.clickable { onResendOTP() })
    }
}

@Composable
fun IconWithCircularBackground(
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .padding(12.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ShowAnimatedLoading(
    topPadding: Int,
    visibility: Boolean
) {
    if (visibility) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding.dp),
            contentAlignment = Alignment.Center,
            content = { LoadingAnimation() }
        )
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

// ***************** PREVIEW *****************************

@Preview(showBackground = true)
@Composable
fun PreviewButton() {
    ButtonWithText(text = "Login", enabled = true) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewEmailTextField() {
    TextFieldComponent(
        placeholder = "user@mail.com",
        contentDescription = "email",
        leadingIcon = Icons.Rounded.Email,
        trailingIcon = Icons.Default.Verified,
        keyboardType = KeyboardType.Email,
        value = "",
        onValueChange = {},
        onChangePasswordVisibility = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUsernameTextField() {
    TextFieldComponent(
        placeholder = "Full Name",
        contentDescription = "Full Name",
        leadingIcon = Icons.Default.Person,
        keyboardType = KeyboardType.Text,
        value = "",
        onValueChange = {},
        onChangePasswordVisibility = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordTextField() {
    TextFieldComponent(
        placeholder = "*******",
        contentDescription = "password",
        leadingIcon = Icons.Rounded.Lock,
        trailingIcon = Icons.Default.RemoveRedEye,
        keyboardType = KeyboardType.Password,
        value = "",
        onValueChange = {},
        onChangePasswordVisibility = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeRemainingAndResendOTP() {
    TimeRemainingAndResendOTP(
        onResendOTP = { /*TODO*/ },
        timeRemaining = "01:23"
    )
}

@Preview
@Composable
fun PreviewIconWithCircularBackground() {
    IconWithCircularBackground(
        icon = Icons.Rounded.ArrowDownward,
        backgroundColor = colorResource(id = R.color.green),
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewShowAnimatedLoading() {
    ShowAnimatedLoading(
        topPadding = 10,
        visibility = true
    )
}