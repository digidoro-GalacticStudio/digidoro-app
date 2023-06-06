package com.galacticstudio.digidoro.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.NOTE_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.NoteScreen

fun NavGraphBuilder.noteNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = Screen.Note.route,
        route = NOTE_GRAPH_ROUTE
    ){
        composable(
            route = Screen.Note.route
        ) {
            NoteScreen(navController = navController)
        }
    }
}