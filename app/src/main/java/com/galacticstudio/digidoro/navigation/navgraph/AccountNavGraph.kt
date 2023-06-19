package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.ACCOUNT_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.NoteScreen
import com.galacticstudio.digidoro.ui.screens.account.AccountScreen

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = Screen.Account.route,
        route = ACCOUNT_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Account.route
        ) {
            AccountScreen(navController = navController)
        }
    }
}