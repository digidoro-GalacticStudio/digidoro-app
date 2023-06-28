package com.galacticstudio.digidoro.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.galacticstudio.digidoro.navigation.ROOT_GRAPH_ROUTE

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    selectionMode: (bottomBarState: MutableState<Boolean>, onRemoveClick: () -> Unit, onDuplicateClick: () -> Unit, onMoveFolderClick: () -> Unit) -> Unit,
    onSelectionChange: (Boolean) -> Unit,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(navController = navController)
        homeNavGraph(navController = navController)
        todoNavGraph(navController = navController)
        noteNavGraph(
            navController = navController,
            selectionMode = selectionMode,
            onSelectionChange = onSelectionChange
        )
        pomodoroNavGraph(navController = navController)
        accountNavGraph(navController = navController)
        userNavGraph(navController = navController)
    }
}


