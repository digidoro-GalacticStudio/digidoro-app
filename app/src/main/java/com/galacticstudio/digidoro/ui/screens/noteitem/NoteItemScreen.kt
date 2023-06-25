package com.galacticstudio.digidoro.ui.screens.noteitem

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.noteitem.components.ColorBottomSheetContent
import com.galacticstudio.digidoro.ui.screens.noteitem.components.DottedDivider
import com.galacticstudio.digidoro.ui.screens.noteitem.components.TagBottomSheetContent
import com.galacticstudio.digidoro.ui.screens.noteitem.components.TransparentTextField
import com.galacticstudio.digidoro.ui.screens.noteitem.viewmodel.NoteItemViewModel
import com.galacticstudio.digidoro.ui.shared.dialogs.ExitConfirmationDialog
import com.galacticstudio.digidoro.ui.shared.floatingCards.BottomSheetLayout
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.ColorCustomUtils
import com.galacticstudio.digidoro.util.ColorCustomUtils.Companion.convertColorToString
import com.galacticstudio.digidoro.util.dropShadow
import kotlinx.coroutines.launch

/**
 * A composable function used for previewing a note item.
 */
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NoteItemScreenPreview() {
    val navController = rememberNavController()
    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NoteItemScreen(
                navController = navController,
                noteColor = Color.White,
                noteId = null,
                folderId = "",
                isReadMode = true
            )
        }
    }
}

/**
 * A composable function representing a note item.
 *
 * @param navController The NavController used for navigation.
 * @param noteColor The color of the note.
 */
@Composable
fun NoteItemScreen(
    navController: NavController,
    noteColor: Color,
    noteId: String?,
    noteItemViewModel: NoteItemViewModel = viewModel(factory = NoteItemViewModel.Factory),
    folderId: String?,
    isReadMode: Boolean?,
) {
    val state = noteItemViewModel.state // Retrieves the current state from the noteItemViewModel.
    val actionType =
        noteItemViewModel.actionTypeEvents // Retrieves the current type of actions from the noteItemViewModel.
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    //Dialogs
    val openExitDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val openMoveToTrashDialog = remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        if (!noteId.isNullOrEmpty()) {
            noteItemViewModel.onEvent(NoteItemEvent.NoteIdChanged(noteId))
        }

        if (!folderId.isNullOrEmpty()) {
            noteItemViewModel.onEvent(NoteItemEvent.FolderIdChanged(folderId))
        }

        noteItemViewModel.onEvent(NoteItemEvent.ColorChanged(convertColorToString(noteColor)))
    }

    /*
     * Executes a side effect using LaunchedEffect when the context value changes.
     * This effect collects response events from the noteItemViewModel and performs different actions based on the event type.
     * -> If the event is of type Error or ErrorWithMessage, displays a toast with the exception message.
     * -> If the event is of type Success, displays a toast with the notes success message and saves the auth token.
     * This effect relies on the provided context to access resources like Toast and the RetrofitApplication instance.
     */
    LaunchedEffect(key1 = context) {
        // Collect the response events from the noteItemViewModel
        noteItemViewModel.responseEvents.collect { event ->
            when (event) {
                is NoteItemResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NoteItemResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is NoteItemResponseState.Success -> {
                    val actionMessage = when (event.actionType) {
                        is ActionType.CreateNote -> "Note added"
                        is ActionType.ReadNote -> "Get Note successfully"
                        is ActionType.DeleteNote -> "Note deleted"
                        is ActionType.UpdateNote -> "Note updated"
                    }

                    Toast.makeText(
                        context,
                        actionMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val newNoteColor =
        Color(android.graphics.Color.parseColor("#${noteItemViewModel.noteColor.value}")).takeUnless { it == Color.White }
            ?: MaterialTheme.colorScheme.background

    TopBarNote(
        snackbarHostState = snackbarHostState,
        noteColor = newNoteColor,
        openExitDialog = openExitDialog,
        openDeleteDialog = openDeleteDialog,
        openMoveToTrashDialog = openMoveToTrashDialog,
        noteItemViewModel = noteItemViewModel,
        isReadMode = isReadMode == true,
        onReturnClick = { navController.popBackStack() },
        content = {
            NoteItemContent(
                noteColor = newNoteColor,
                noteItemViewModel = noteItemViewModel,
                isReadMode = isReadMode == true
            )
        },
    )

    ExitConfirmationDialog(
        title = "Exit Confirmation",
        text = "You have unsaved changes. Do you want to save them?",
        openDialog = openExitDialog,
        onConfirmClick = {
            when (actionType.value) {
                is ActionType.CreateNote -> {
                    noteItemViewModel.onEvent(NoteItemEvent.SaveNote)
                }

                is ActionType.UpdateNote -> {
                    noteItemViewModel.onEvent(NoteItemEvent.UpdateNote)
                }

                else -> {}
            }

            if (noteItemViewModel.state.value.noteError == null) {
                navController.navigate(Screen.Note.route)
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Error: ${noteItemViewModel.state.value.noteError}"
                    )
                }
            }
            openExitDialog.value = false
        },
        onDismissClick = {
            openExitDialog.value = false
            navController.popBackStack()
        },
        onDismissRequest = { openExitDialog.value = false }
    )

    ExitConfirmationDialog(
        title = "Delete Confirmation",
        text = "Do you want to permanently delete this note?",
        openDialog = openDeleteDialog,
        onConfirmClick = {
            noteItemViewModel.onEvent(NoteItemEvent.DeleteNote)
            openDeleteDialog.value = false
            navController.popBackStack()
        },
        onDismissClick = {
            openDeleteDialog.value = false
        },
        onDismissRequest = { openDeleteDialog.value = false }
    )

    ExitConfirmationDialog(
        title = "Move to Trash Confirmation",
        text = "Do you want to move this note to the Trash?",
        openDialog = openMoveToTrashDialog,
        onConfirmClick = {
            noteItemViewModel.onEvent(NoteItemEvent.ToggleTrash)
            openMoveToTrashDialog.value = false
            navController.popBackStack()
        },
        onDismissClick = {
            openMoveToTrashDialog.value = false
            navController.popBackStack()
        },
        onDismissRequest = { openMoveToTrashDialog.value = false }
    )
}


/**
 * A composable function representing the content of a note item.
 *
 * @param noteColor The color of the note.
 */
@Composable
fun NoteItemContent(
    noteColor: Color,
    noteItemViewModel: NoteItemViewModel,
    isReadMode: Boolean,
) {
    val state = noteItemViewModel.state

    val colorFilter = if (noteColor != Color.White) {
        ColorCustomUtils.addColorTone(MaterialTheme.colorScheme.tertiaryContainer, noteColor)
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFilter)
            .wrapContentSize(Alignment.Center)
    ) {
        val color = ColorCustomUtils.returnLuminanceColor(noteColor)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(noteColor),
        ) {
            TransparentTextField(
                text = state.value.message,
                hint = "Hint",
                readOnly = isReadMode,
                modifier = Modifier.padding(36.dp),
                applyFillMaxHeight = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = color
                ),
                hintStyle = MaterialTheme.typography.bodyLarge,
                hintColor = color.copy(0.45f),
            ) { noteItemViewModel.onEvent(NoteItemEvent.ContentChanged(it)) }
        }
    }
}

/**
 * A composable function representing the top bar of a note.
 *
 * @param content The content of the note item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNote(
    content: @Composable () -> Unit = {},
    noteColor: Color,
    snackbarHostState: SnackbarHostState,
    openExitDialog: MutableState<Boolean>,
    openDeleteDialog: MutableState<Boolean>,
    openMoveToTrashDialog: MutableState<Boolean>,
    noteItemViewModel: NoteItemViewModel,
    isReadMode: Boolean,
    onReturnClick: () -> Boolean,
) {
    val state = noteItemViewModel.state
    var showMenu by remember { mutableStateOf(false) }
    val color = ColorCustomUtils.returnLuminanceColor(noteColor)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TransparentTextField(
                        text = state.value.title,
                        hint = "Change Title",
                        singleLine = true,
                        readOnly = isReadMode,
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = color
                        ),
                        hintStyle = MaterialTheme.typography.titleLarge,
                        hintColor = color.copy(alpha = 0.45f),
                        fontWeight = FontWeight.W700,
                    ) { noteItemViewModel.onEvent(NoteItemEvent.TitleChanged(it)) }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!isReadMode) openExitDialog.value = true else onReturnClick()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = color
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = noteColor
                ),
                actions = {
                    if (isReadMode) {
                        //If the note is trashed
                        TextButton(
                            onClick = {
                                noteItemViewModel.onEvent(NoteItemEvent.ToggleTrash)
                                onReturnClick()
                            }
                        ) {
                            Text(
                                text = "Restore",
                                style = MaterialTheme.typography.titleMedium,
                                color = color
                            )
                        }
                        TextButton(
                            onClick = { openDeleteDialog.value = true }
                        ) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.titleMedium,
                                color = color
                            )
                        }
                    } else {
                        // If the note is in write mode
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = color
                            )
                        }
                        DropDownNoteMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            noteItemViewModel = noteItemViewModel,
                            onDeleteNote = { openMoveToTrashDialog.value = true },
                            onToggleFavoriteNote = { noteItemViewModel.onEvent(NoteItemEvent.ToggleFavorite) }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            content()
        }
    }
}

/**
 * A composable function representing the dropdown menu for a note.
 *
 * @param expanded Whether the dropdown menu is expanded or not.
 * @param onDismissRequest The callback for dismissing the dropdown menu.
 */
@Composable
fun DropDownNoteMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteNote: () -> Unit,
    onToggleFavoriteNote: () -> Unit,
    noteItemViewModel: NoteItemViewModel,
) {
    var openTagBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openFolderBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openColorBottomSheet by rememberSaveable { mutableStateOf(false) }

    val isUpdated = when (noteItemViewModel.actionTypeEvents.value) {
        is ActionType.UpdateNote -> true
        else -> false
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier
            .dropShadow(
                color = MaterialTheme.colorScheme.onPrimary.copy(0.25f),
                blurRadius = 5.dp,
            )
            .padding(2.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {

        Text(
            text = "Configuration",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W800,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
        DropdownMenuItem(
            text = { Text("Folders", color = MaterialTheme.colorScheme.onPrimary) },
            onClick = { openFolderBottomSheet = true }
        )
        DropdownMenuItem(
            text = { Text("Tags", color = MaterialTheme.colorScheme.onPrimary) },
            onClick = { openTagBottomSheet = true }
        )
        DropdownMenuItem(
            text = { Text("Colors", color = MaterialTheme.colorScheme.onPrimary) },
            onClick = { openColorBottomSheet = true }
        )
        DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
        Row(
            Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownMenuItem(
                modifier = Modifier.width(50.dp),
                enabled = isUpdated,
                text = {
                    val isFavorite = !noteItemViewModel.state.value.isFavorite
                    Icon(
                        painter = if (isFavorite) painterResource(R.drawable.heart_border_icon) else
                            painterResource(R.drawable.heart_fill_icon),
                        contentDescription = "Favorite Note",
                        modifier = Modifier.size(25.dp),
                        tint = if (isFavorite) MaterialTheme.colorScheme.onPrimary else
                            Color.Red,
                    )
                },
                onClick = { onToggleFavoriteNote() }
            )
            Spacer(modifier = Modifier.width(75.dp))
            DropdownMenuItem(
                modifier = Modifier.width(50.dp),
                enabled = isUpdated,
                text = {
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "Delete Note",
                        modifier = Modifier.size(25.dp),
                        tint = if (isUpdated) MaterialTheme.colorScheme.onPrimary else
                            MaterialTheme.colorScheme.onPrimary.copy(0.7f),
                    )
                },
                onClick = { onDeleteNote() }
            )
        }
    }
    BottomSheetLayout(
        openBottomSheet = openTagBottomSheet,
        onDismissRequest = { openTagBottomSheet = false },
        content = { TagBottomSheetContent() },
    )
    // TODO REVIEW THIS
//    BottomSheetLayout(
//        openBottomSheet = openFolderBottomSheet,
//        onDismissRequest = { openFolderBottomSheet = false },
//        content = { FolderBottomSheetContent(openFolderBottomSheet, notesViewModel) },
//    )
    BottomSheetLayout(
        openBottomSheet = openColorBottomSheet,
        onDismissRequest = { openColorBottomSheet = false },
        content = {
            ColorBottomSheetContent(
                noteItemViewModel = noteItemViewModel,
                onBottomSheetChanged = { newValue -> openColorBottomSheet = newValue },
            )
        }
    )
}