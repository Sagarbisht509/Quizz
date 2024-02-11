package com.sagar.quizz.presentation.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sagar.quizz.R
import com.sagar.quizz.presentation.components.ButtonWithText
import com.sagar.quizz.presentation.components.HeightSpacer
import com.sagar.quizz.presentation.components.ShowAnimatedLoading
import com.sagar.quizz.ui.theme.Quicksand

@Composable
fun AvatarContent(
    avatars: List<String>,
    selectedAvatarIndex: Int,
    isLoading: Boolean,
    onAvatarChanged: (Int) -> Unit,
    registerButtonClicked: () -> Unit,
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
            text = stringResource(R.string.select_avatar),
            fontFamily = Quicksand,
            fontWeight = FontWeight.Medium,
            fontSize = 26.sp
        )

        HeightSpacer(value = 15)

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 3),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(avatars.size) { index ->

                val isSelected = selectedAvatarIndex == index
                val alpha = if (isSelected) 1f else 0.3f

                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(if (isSelected) 115.dp else 100.dp)
                        .graphicsLayer(alpha = alpha)
                        .clickable(
                            onClick = { onAvatarChanged(index) },
                            enabled = !isLoading
                        )
                ) {
                    AsyncImage(
                        model = avatars[index],
                        placeholder = painterResource(id = R.drawable.blank_avatar),
                        contentDescription = stringResource(R.string.user_profile),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        HeightSpacer(value = 20)

        ButtonWithText(
            text = stringResource(R.string.register),
            modifier = Modifier.padding(5.dp),
            enabled = !isLoading,
            onClick = registerButtonClicked
        )

        ShowAnimatedLoading(topPadding = 15, visibility = isLoading)
    }
}