package com.galacticstudio.digidoro.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.galacticstudio.digidoro.navigation.ROOT_GRAPH_ROUTE
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel.PomodoroViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    selectionMode: (bottomBarState: MutableState<Boolean>, onRemoveClick: () -> Unit, onDuplicateClick: () -> Unit, onMoveFolderClick: () -> Unit) -> Unit,
    onSelectionChange: (Boolean) -> Unit,
    startDestination: String,
    stopwatchService: TimerService,
    pomodoroViewModel: PomodoroViewModel,
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
        pomodoroNavGraph(
            navController = navController,
            pomodoroViewModel = pomodoroViewModel,
            stopwatchService = stopwatchService
        )
        accountNavGraph(navController = navController)
        userNavGraph(navController = navController)
    }
}


