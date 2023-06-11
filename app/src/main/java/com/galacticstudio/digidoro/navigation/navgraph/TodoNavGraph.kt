package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.navigation.TODO_GRAPH_ROUTE
import com.galacticstudio.digidoro.ui.TodoScreen

fun NavGraphBuilder.todoNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = Screen.Todo.route,
        route = TODO_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Todo.route
        ) {
            TodoScreen(navController = navController)
        }
    }
}