package com.galacticstudio.digidoro.navigation.navgraph

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.galacticstudio.digidoro.navigation.NOTE_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemScreen
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesListScreen

fun NavGraphBuilder.noteNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Note.route,
        route = NOTE_GRAPH_ROUTE
    ) {
        composable(
            route = Screen.Note.route
        ) {
            NotesListScreen(navController = navController)
        }

        composable(
            route = Screen.Note.route + "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val color = Color(it.arguments?.getInt("noteColor") ?: 0)
            NoteItemScreen(
                navController = navController,
                noteColor = color
            )
        }
    }
}