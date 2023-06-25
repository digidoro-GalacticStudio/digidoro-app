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
            route = Screen.Note.route + "?noteId={noteId}&noteColor={noteColor}&isReadMode={isReadMode}&folderId={folderId}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "isReadMode"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(
                    name = "folderId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            val color = Color(it.arguments?.getInt("noteColor") ?: 0)
            val noteId = it.arguments?.getString("noteId")
            val folderId = it.arguments?.getString("folderId")
            val isReadMode = it.arguments?.getBoolean("isReadMode")

            NoteItemScreen(
                navController = navController,
                noteColor = color,
                noteId = noteId,
                folderId = folderId,
                isReadMode = isReadMode,
            )
        }
    }
}