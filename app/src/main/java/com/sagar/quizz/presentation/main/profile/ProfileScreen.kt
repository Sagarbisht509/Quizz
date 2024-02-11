package com.sagar.quizz.presentation.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.ShowAlertDialog
import com.sagar.quizz.presentation.components.WidthSpacer
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuthScreen: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            HeightSpacer(value = 25)
            UserDetails(uiState)
            HeightSpacer(value = 20)
            QuizDetails(uiState)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            TextWithLeadingIcon(
                text = stringResource(R.string.about),
                icon = painterResource(id = R.drawable.ic_about),
                contentDescription = stringResource(R.string.about),
                onClick = {}
            )
            HeightSpacer(value = 15)
            TextWithLeadingIcon(
                text = stringResource(R.string.logout),
                icon = painterResource(id = R.drawable.ic_logout),
                contentDescription = stringResource(R.string.logout),
                onClick = { viewModel.changeLogoutDialogState(value = true) }
            )
        }
    }

    when {
        uiState.openAlertDialog -> {
            ShowAlertDialog(
                onDismissRequest = { viewModel.changeLogoutDialogState(value = false) },
                onConfirmation = {
                    viewModel.apply {
                        changeLogoutDialogState(value = false)
                        onLogoutConfirmed()
                    }
                    onNavigateToAuthScreen()
                },
                dialogTitle = stringResource(R.string.confirm_logout),
                dialogText = stringResource(R.string.are_you_sure_you_want_to_logout)
            )
        }

        uiState.logoutCompleted -> {
            onNavigateToAuthScreen()
        }
    }

}

@Composable
fun TextWithLeadingIcon(
    text: String,
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(
            onClick = onClick,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = icon, contentDescription = contentDescription)
        WidthSpacer(value = 16)
        Text(
            text = text,
            fontFamily = Quicksand
        )
    }
}

@Composable
fun QuizDetails(state: ProfileState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.green)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ColumnText(
                count = state.quizAttempts.toString(),
                text = stringResource(R.string.attempts)
            )
            ColumnText(
                count = state.quizCreated.toString(),
                text = stringResource(R.string.created)
            )
            ColumnText(count = state.exp.toString(), text = stringResource(R.string.exp))
        }
    }
}

@Composable
fun ColumnText(
    count: String,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            color = Color.White,
            fontFamily = Quicksand,
            fontSize = 16.sp
        )
        HeightSpacer(value = 8)
        Text(
            text = text,
            color = Color.White,
            fontFamily = Quicksand,
        )
    }
}

@Composable
fun UserDetails(state: ProfileState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = state.profilePic,
            placeholder = painterResource(id = R.drawable.blank_avatar),
            contentDescription = stringResource(R.string.user_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        HeightSpacer(value = 10)
        Text(
            text = state.userName,
            fontFamily = Quicksand,
            fontSize = 22.sp,
        )
        HeightSpacer(value = 6)
        Text(
            text = state.email,
            fontFamily = Quicksand,
            color = Color.Gray
        )
    }
}


// ******************* PREVIEW *******************

/*
@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}*/
