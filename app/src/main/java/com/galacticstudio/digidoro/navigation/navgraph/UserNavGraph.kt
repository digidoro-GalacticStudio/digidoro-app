package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.navigation.USER_GRAPH_ROUTE
import com.galacticstudio.digidoro.ui.screens.edituser.EditUserScreen

fun NavGraphBuilder.userNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Login.route,
        route = USER_GRAPH_ROUTE
    ) {
        composable(
            route = Screen.EditUser.route
        ) {
            EditUserScreen(navController = navController)
        }
    }
}