package com.galacticstudio.digidoro.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.galacticstudio.digidoro.R

/**
 * Sealed class representing items in the menu.
 *
 * @param icon The resource ID of the icon for the item.
 * @param title The title of the item.
 * @param route The route associated with the item.
 */
sealed class ItemsMenu(
    val icon: Int,
    val title: String,
    val route: String
) {
    object HomeItem : ItemsMenu(
        R.drawable.location_automation,
        "home",
        "home_screen"
    )

    object TodoItem : ItemsMenu(
        R.drawable.todo,
        "todo",
        "todo_screen"
    )

    object NotesItem : ItemsMenu(
        R.drawable.notes,
        "notes",
        "note_screen"
    )

    object PomosItem : ItemsMenu(
        R.drawable.pomo,
        "pomos",
        "pomodoro_screen"
    )

    object AccountItem : ItemsMenu(
        R.drawable.manage_account_icon,
        "account",
        "account_screen"
    )
}

/**
 * A composable function that displays the app scaffold.
 *
 * @param navController The NavHostController used for navigation.
 * @param content The content of the app scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 23.dp) {
                BottomBarNavigation(navController = navController)
            }
        },
        modifier = Modifier.shadow(45.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
        ) {
            content()
        }

    }
}

/**
 * A composable function that displays the bottom navigation bar.
 *
 * @param navController The NavHostController used for navigation.
 */
@Composable
fun BottomBarNavigation(navController: NavHostController) {
    val navigationItems = listOf(
        ItemsMenu.HomeItem,
        ItemsMenu.TodoItem,
        ItemsMenu.NotesItem,
        ItemsMenu.PomosItem,
        ItemsMenu.AccountItem
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = Color.White
    ) {
        navigationItems.forEach { item ->
            val seletedRoute = item.route == navBackStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = seletedRoute,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(23.dp)
                    )
                },
                label = { Text(item.title) },
                alwaysShowLabel = false
            )
        }
    }
}
