package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.ACCOUNT_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.POMODORO_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.RANKING_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.NoteScreen
import com.galacticstudio.digidoro.ui.screens.account.AccountScreen
import com.galacticstudio.digidoro.ui.screens.account.components.premium.PremiumScreen
import com.galacticstudio.digidoro.ui.screens.login.LoginScreen
import com.galacticstudio.digidoro.ui.screens.ranking.RankingScreen
import com.galacticstudio.digidoro.ui.screens.todo.TodoScreen

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
        composable(
            route = Screen.PremiumUser.route
        ) {
            PremiumScreen(navController = navController)
        }
    }
    navigation(
        startDestination = Screen.Ranking.route,
        route = RANKING_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Ranking.route
        ) {
            RankingScreen(navController = navController)
        }
    }
}