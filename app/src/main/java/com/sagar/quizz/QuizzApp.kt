package com.sagar.quizz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sagar.quizz.data.model.local.NavigationItem
import com.sagar.quizz.presentation.navigation.Destination
import com.sagar.quizz.presentation.navigation.MainNavGraph
import com.sagar.quizz.ui.theme.QuizzTheme

@Composable
fun QuizzApp(navController: NavHostController = rememberNavController()) {

    QuizzTheme {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .statusBarsPadding()
            ) {
                MainNavGraph(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    navController: NavHostController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val bottomBarDestination = BottomBarParams.bottomMenu.any { it.route == currentDestination }

    AnimatedVisibility(
        visible = bottomBarDestination,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar(
            modifier = Modifier
                .graphicsLayer {
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                    clip = true
                },
            containerColor = colorResource(id = R.color.background),
        ) {
            BottomBarParams.bottomMenu.forEach { item ->
                NavigationBarItem(
                    selected = currentDestination == item.route,
                    onClick = {
                        navController.navigate(route = item.route) {
                            popUpTo(route = Destination.Main.Home.route) {
                                saveState = true
                                inclusive = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount != null) {
                                    Badge(
                                        modifier = Modifier.size(5.dp),
                                        containerColor = Color.Red
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.green),
                        unselectedIconColor = Color.Gray,
                        indicatorColor = colorResource(id = R.color.background)
                    )
                )
            }
        }
    }
}

object BottomBarParams {
    val bottomMenu = listOf(
        NavigationItem(
            title = "Home",
            route = "Home",
            icon = R.drawable.nav_home
        ),

        NavigationItem(
            title = "Search",
            route = "Search",
            icon = R.drawable.nav_search
        ),

        NavigationItem(
            title = "Favourite",
            route = "Favourite",
            icon = R.drawable.nav_fav
        ),

        NavigationItem(
            title = "Profile",
            route = "Profile",
            icon = R.drawable.nav_profile,
            badgeCount = 1
        )
    )
}