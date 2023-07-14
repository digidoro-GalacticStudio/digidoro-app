package com.galacticstudio.digidoro.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.ui.screens.MainViewModel
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.navgraph.SetupNavGraph
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.ui.screens.noteslist.components.ActionsBottomBar
import com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel.PomodoroViewModel
import com.galacticstudio.digidoro.ui.theme.Gray30
import com.galacticstudio.digidoro.util.WindowSize
import com.galacticstudio.digidoro.util.dropShadow

@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScaffoldPreview() {
    //DigidoroTheme { AppScaffold(rememberNavController(), context) }
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

    object PomodoroItem : ItemsMenu(
        R.drawable.pomo,
        "pomos",
        "pomodoro_screen"
    )

    object AccountItem : ItemsMenu(
        R.drawable.manage_account_icon,
        "account",
        "account_screen"
//        "login_screen"
    )
}

/**
 * Composable function for the main app scaffold.
 *
 * @param navController The NavHostController used for navigation.
 * @param mainViewModel The MainViewModel used for app data and state.
 */
@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    stopwatchService: TimerService,
    pomodoroViewModel: PomodoroViewModel
) {
    //Logged status
    val isLoggedIn = mainViewModel.hasToken()
    val startDestination = if (isLoggedIn) HOME_GRAPH_ROUTE else AUTH_GRAPH_ROUTE

    // State of bottomBar
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Determine whether to show the bottom bar based on the current destination route.
    bottomBarState.value = when (navBackStackEntry?.destination?.route) {
        in listOf(
            "home_screen",
            "todo_screen",
            "pomodoro_screen",
            "note_screen",
        ) -> true

        else -> false
    }

    //State of selection bar
    val modeState: SelectionModeAppState = rememberSelectionModeAppState()

    val selectionBarState = rememberSaveable { (mutableStateOf(false)) }

    val screenSize = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                selectionBarState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                ActionsBottomBar(
                    selectionBarState,
                    onRemoveClick = { modeState.onRemoveClick() },
                    onDuplicateClick = { modeState.onDuplicateClick() },
                    onMoveFolderClick = { modeState.onMoveFolderClick() }
                )
            }

            AnimatedVisibility(
                !selectionBarState.value && (screenSize < WindowSize.COMPACT),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomBarNavigation(
                    navController = navController,
                    bottomBarState = bottomBarState,
                )
            }
        },
    ) {
        if (screenSize > WindowSize.MEDIUM) {
            PermanentNavigationBar(
                navController,
                bottomBarState
            ) {
                AppContent(
                    navController = navController,
                    modeState,
                    startDestination,
                    selectionBarState,
                    stopwatchService,
                    pomodoroViewModel,
                )
            }
        } else if ((screenSize >= WindowSize.COMPACT) && (screenSize <= WindowSize.MEDIUM)) {
            NavigationBarRail(navController, bottomBarState) {
                AppContent(
                    navController = navController,
                    modeState,
                    startDestination,
                    selectionBarState,
                    stopwatchService,
                    pomodoroViewModel,
                )
            }
        } else {
            AppContent(
                navController = navController,
                modeState,
                startDestination,
                selectionBarState,
                stopwatchService,
                pomodoroViewModel,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent(
    navController: NavHostController,
    modeState: SelectionModeAppState,
    startDestination: String,
    selectionBarState: MutableState<Boolean>,
    stopwatchService: TimerService,
    pomodoroViewModel: PomodoroViewModel,
) {
    Surface {
        SetupNavGraph(
            navController = navController,
            startDestination = startDestination,
            selectionMode = { bottomBarState, onRemoveClick, onDuplicateClick, onMoveFolderClick ->
                selectionBarState.value = bottomBarState.value
                modeState.onRemoveClick = onRemoveClick
                modeState.onDuplicateClick = onDuplicateClick
                modeState.onMoveFolderClick = onMoveFolderClick
            },
            onSelectionChange = { isSelectionMode ->
                selectionBarState.value = isSelectionMode
            },
            stopwatchService = stopwatchService,
            pomodoroViewModel = pomodoroViewModel,
        )
    }
}

class SelectionModeAppState(
    var selectionState: MutableState<Boolean>,
    val navController: NavHostController,
    var onDuplicateClick: () -> Unit,
    var onRemoveClick: () -> Unit,
    var onMoveFolderClick: () -> Unit,
)

@SuppressLint("RememberReturnType")
@Composable
fun rememberSelectionModeAppState(
    selectionState: MutableState<Boolean> = remember { mutableStateOf(false) },
    navController: NavHostController = rememberNavController(),
    onDuplicateClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onMoveFolderClick: () -> Unit = {},
) = remember(selectionState, navController, onDuplicateClick, onRemoveClick, onMoveFolderClick) {
    SelectionModeAppState(
        selectionState = selectionState,
        navController = navController,
        onDuplicateClick = onDuplicateClick,
        onRemoveClick = onRemoveClick,
        onMoveFolderClick = onMoveFolderClick,
    )
}

val navigationItems = listOf(
    ItemsMenu.HomeItem,
    ItemsMenu.TodoItem,
    ItemsMenu.NotesItem,
    ItemsMenu.PomodoroItem,
    ItemsMenu.AccountItem
)

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
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            Surface(
                modifier = Modifier
                    .height(80.dp)
                    .dropShadow(
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

@Composable
fun NavigationBarRail(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Row {
        AnimatedVisibility(
            bottomBarState.value,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it }),
        ) {

            Surface(
                modifier = Modifier
                    .dropShadow(
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.25f),
                        blurRadius = 4.dp
                    )
            ) {
                NavigationRail {
                    navigationItems.forEach { item ->
                        val selectedRoute = item.route == navBackStackEntry?.destination?.route

                        NavigationRailItem(
                            modifier = Modifier.padding(top = 10.dp),
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(23.dp)
                                )
                            },
                            label = { Text(item.title) },
                            selected = selectedRoute,
                            onClick = {
                                navController.navigate(item.route)
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = Gray30,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                    }
                }
            }
        }

        content()
    }
}

@Composable
fun PermanentNavigationBar(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet() {
                navigationItems.forEach { item ->
                    val selectedRoute = item.route == navBackStackEntry?.destination?.route
                    NavigationDrawerItem(
                        modifier = Modifier.padding(top = 10.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(23.dp)
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedRoute,
                        onClick = { navController.navigate(item.route) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedIconColor = Gray30,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                }
            }
        }
    ) {
        content()
    }
}