package com.galacticstudio.digidoro.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.ROOT_GRAPH_ROUTE

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HOME_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(navController = navController)
        homeNavGraph(navController = navController)
        todoNavGraph(navController = navController)
        noteNavGraph(navController = navController)
        pomodoroNavGraph(navController = navController)
        accountNavGraph(navController = navController)
    }
}


