package com.galacticstudio.digidoro.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.POMODORO_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.pomodoroNavGraph(
    navController: NavHostController,
    stopwatchService: TimerService
){
    navigation(
        startDestination = Screen.Pomodoro.route,
        route = POMODORO_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Pomodoro.route
        ) {
            PomodoroScreen(
                navController = navController,
                stopwatchService = stopwatchService
            )
        }
    }
}