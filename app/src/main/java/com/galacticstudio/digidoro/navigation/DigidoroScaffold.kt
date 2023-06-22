package com.galacticstudio.digidoro.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.navgraph.SetupNavGraph
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.ui.theme.Gray30
import com.galacticstudio.digidoro.util.dropShadow

@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScaffoldPreview() {
    DigidoroTheme { AppScaffold(rememberNavController()) }
}

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
//        "account_screen"
        "login_screen"
    )
}

/**
 * A composable function that displays the app scaffold.
 *
 * @param navController The NavHostController used for navigation.
 * @param content The content of the app scaffold.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold(navController: NavHostController) {

    // State of bottomBar
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Determine whether to show the bottom bar based on the current destination route.
    bottomBarState.value = when (navBackStackEntry?.destination?.route) {
        in listOf("home_screen", "todo_screen", "pomodoro_screen", "note_screen") -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            BottomBarNavigation(
                navController = navController,
                bottomBarState = bottomBarState,
            )
        },
    ) {
        SetupNavGraph(navController = navController)
    }
}

/**
 * A composable function that displays the bottom navigation bar.
 *
 * @param navController The NavHostController used for navigation.
 */
@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val navigationItems = listOf(
        ItemsMenu.HomeItem,
        ItemsMenu.TodoItem,
        ItemsMenu.NotesItem,
        ItemsMenu.PomosItem,
        ItemsMenu.AccountItem
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            Surface(
                modifier = Modifier.dropShadow(
                    color = MaterialTheme.colorScheme.onPrimary.copy(0.25f),
                    blurRadius = 4.dp
                )
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    navigationItems.forEach { item ->
                        val selectedRoute = item.route == navBackStackEntry?.destination?.route
                        NavigationBarItem(
                            selected = selectedRoute,
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
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Gray30,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                    }
                }
            }
        }
    )
}
