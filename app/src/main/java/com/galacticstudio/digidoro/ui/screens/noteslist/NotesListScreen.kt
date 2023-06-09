package com.galacticstudio.digidoro.ui.screens.noteslist

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.noteitem.components.FolderBottomSheetContent
import com.galacticstudio.digidoro.ui.screens.noteitem.components.MoveToFolderBottomSheet
import com.galacticstudio.digidoro.ui.screens.noteslist.components.ActionNote
import com.galacticstudio.digidoro.ui.screens.noteslist.components.ActionsTopBar
import com.galacticstudio.digidoro.ui.screens.noteslist.components.FolderItem
import com.galacticstudio.digidoro.ui.screens.noteslist.components.NoteItem
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.FolderResponseState
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.FolderViewModel
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.NotesViewModel
import com.galacticstudio.digidoro.ui.screens.todo.list.TodosEvent
import com.galacticstudio.digidoro.ui.shared.dialogs.ExitConfirmationDialog
import com.galacticstudio.digidoro.ui.shared.floatingCards.BottomSheetLayout
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.DateUtils
import com.galacticstudio.digidoro.util.WindowSize
import com.galacticstudio.digidoro.util.shimmerEffect
import com.galacticstudio.digidoro.work.SyncStatus
import com.galacticstudio.digidoro.work.SynchronizationWorker
import java.util.Date

/**
 * Preview function for the notes list screen..
 */
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesListSPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            NotesListScreen(navController = navController)
        }
    }
}

data class ActionNoteData(
    val text: String,
    val leadingIcon: Painter,
    val colorIcon: Color,
    val onClick: () -> Unit,
    val resultMode: NoteResultsMode,
)

enum class CardState {
    LOADING,
    SELECTED,
    DESELECTED,
    NORMAL,
}

/**
 * A composable function representing the note list screen.
 *
 * @param navController The navigation controller used for navigating to different screens.
 */


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesListScreen(
    navController: NavHostController,
    notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory),
    folderViewModel: FolderViewModel = viewModel(factory = FolderViewModel.Factory),
    selectionMode: (
        bottomBarState: MutableState<Boolean>,
        onRemoveClick: () -> Unit,
        onDuplicateClick: () -> Unit,
        onMoveFolderClick: () -> Unit
    ) -> Unit,
    onSelectionChange: (Boolean) -> Unit,
) {
    // State of actions bottomBar
    val isSelectionMode = remember { mutableStateOf(false) }
    val selectedCard = remember { mutableStateOf<NoteModel?>(null) }
    //Move to trash and remove note dialog
    val openDeleteDialog = remember { mutableStateOf(false) }
    val openMoveToTrashDialog = remember { mutableStateOf(false) }
    val openMoveToFolderDialog = remember { mutableStateOf(false) }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val app: Application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current

    val screenSize = LocalConfiguration.current.screenWidthDp.dp

    // Custom component to handle back events
    BackHandler(enabled = isSelectionMode.value) {
        if (isSelectionMode.value) {
            isSelectionMode.value = false
            onSelectionChange(false)
            selectedCard.value = null
        } else {
            backDispatcher?.onBackPressed()
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val previousDestination = remember { mutableStateOf<NavDestination?>(null) }

    LaunchedEffect(currentBackStackEntry?.destination) {
        if (currentBackStackEntry?.destination != previousDestination.value) {
            isSelectionMode.value = false
            onSelectionChange(false)
            selectedCard.value = null
        }
    }

    val syncStatus = SynchronizationWorker.syncStatusLiveData.observeAsState(initial = SyncStatus.INITIAL)

    LaunchedEffect(syncStatus.value) {
        if (syncStatus.value == SyncStatus.COMPLETED) {
            Toast.makeText(
                context,
                "Synchronization finished",
                Toast.LENGTH_SHORT
            ).show()
            notesViewModel.onEvent(NotesEvent.Rebuild(notesViewModel.resultsMode.value))

            SynchronizationWorker.syncStatusLiveData.value = SyncStatus.INITIAL
        }
        if (syncStatus.value == SyncStatus.IN_PROGRESS) {
            Toast.makeText(
                context,
                "Synchronization started",
                Toast.LENGTH_SHORT
            ).show()
            notesViewModel.onEvent(NotesEvent.LoadingChanged(true))
        }
    }

    LaunchedEffect(Unit) {
        selectionMode(
            isSelectionMode,
            {
                //If the mode is all notes, move to trash
                if (notesViewModel.resultsMode.value == NoteResultsMode.TrashNotes) {
                    openDeleteDialog.value = true
                } else {
                    openMoveToTrashDialog.value = true
                }
            },
            {
                //Duplicate Note event
                notesViewModel.onEvent(NotesEvent.DuplicateNote(selectedCard.value))
                isSelectionMode.value = false
                onSelectionChange(false)
                selectedCard.value = null
            },
            {
                // Move to another folder event
                if (app.getRoles().contains("premium")) {
                        notesViewModel.onEvent(
                            NotesEvent.GetSelectedFolder(
                                selectedCard.value?.id ?: ""
                            )
                        )
                        openMoveToFolderDialog.value = true
                } else {
                    Toast.makeText(
                        context,
                        "You are not a PRO user. Upgrade your account to unlock PRO features",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        notesViewModel.onEvent(NotesEvent.RolesChanged(app.getRoles()))
        notesViewModel.onEvent(NotesEvent.Rebuild(NoteResultsMode.AllNotes))
    }

    LaunchedEffect(key1 = context) {
        // Collect the response events from the notesViewModel
        notesViewModel.responseEvents.collect { event ->
            when (event) {
                is NotesResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NotesResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {
//                    if (syncStatus.value != SyncStatus.IN_PROGRESS)
//                        notesViewModel.onEvent(NotesEvent.Rebuild(notesViewModel.resultsMode.value))
                }
            }
        }

        // Collect the response events from the folderViewModel
        folderViewModel.responseEvents.collect { event ->
            when (event) {
                is FolderResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is FolderResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    val topOffSet = if (screenSize < WindowSize.COMPACT) (-85).dp else (-5).dp
    Scaffold(
        modifier = Modifier.zIndex(10f),
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isSelectionMode.value,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it }),
            ) {
                FloatingActionButton(
                    onClick = {
                        val folderId = notesViewModel.state.value.selectedFolder?.id ?: ""
                        val color = Color.White.toArgb()
                        navController.navigate(Screen.Note.route + "?noteId=&noteColor=${color}&folderId=${folderId}")
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .offset(y = topOffSet)
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ad_note_icon),
                        contentDescription = "Add note",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        },
        topBar = {
            ActionsTopBar(
                visible = isSelectionMode.value,
                onReturnClick = {
                    isSelectionMode.value = false
                    onSelectionChange(false)
                    selectedCard.value = null
                }
            )
        },
        bottomBar = {

        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NotesListContent(
                navController = navController,
                notesViewModel = notesViewModel,
                folderViewModel = folderViewModel,
                isSelectionMode = isSelectionMode,
                selectedCard = selectedCard,
                onLongNoteClick = {
                    onSelectionChange(true)
                },
            )
        }
    }

    BottomSheetLayout(
        openBottomSheet = openMoveToFolderDialog.value,
        onDismissRequest = { openMoveToFolderDialog.value = false },
        content = {
            MoveToFolderBottomSheet(
                notesViewModel = notesViewModel,
                openMoveToFolderDialog = openMoveToFolderDialog,
                onSuccessClick = {
                    isSelectionMode.value = false
                    onSelectionChange(false)
                    selectedCard.value = null
                }
            )
        },
    )

    ExitConfirmationDialog(
        title = "Delete Confirmation",
        text = "Do you want to permanently delete this note?",
        openDialog = openDeleteDialog,
        onConfirmClick = {
            notesViewModel.onEvent(NotesEvent.DeleteNote(selectedCard.value?.id ?: ""))
            openDeleteDialog.value = false
            isSelectionMode.value = false
            onSelectionChange(false)
            selectedCard.value = null
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
            notesViewModel.onEvent(NotesEvent.ToggleTrash(selectedCard.value?.id ?: ""))
            openMoveToTrashDialog.value = false
            isSelectionMode.value = false
            onSelectionChange(false)
            selectedCard.value = null
        },
        onDismissClick = {
            openMoveToTrashDialog.value = false
        },
        onDismissRequest = { openMoveToTrashDialog.value = false }
    )
}

@Composable
fun NotesListContent(
    navController: NavHostController,
    notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory),
    folderViewModel: FolderViewModel,
    isSelectionMode: MutableState<Boolean>,
    selectedCard: MutableState<NoteModel?>,
    onLongNoteClick: () -> Unit
) {
    val app: Application = LocalContext.current.applicationContext as Application
    val state = notesViewModel.state.value

    val actionNotesList = listOf(
        ActionNoteData(
            text = "All notes",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFF4880FF),
            resultMode = NoteResultsMode.AllNotes,
            onClick = {
                notesViewModel.onEvent(NotesEvent.ResultsChanged(NoteResultsMode.AllNotes))
            }
        ),
        ActionNoteData(
            text = "Your folders",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFF202124),
            resultMode = NoteResultsMode.FolderNotes,
            onClick = {
                notesViewModel.onEvent(NotesEvent.ResultsChanged(NoteResultsMode.FolderNotes))
            },
        ),
        ActionNoteData(
            text = "Your favorites",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFFFFC700),
            resultMode = NoteResultsMode.FavoriteNotes,
            onClick = {
                notesViewModel.onEvent(NotesEvent.ResultsChanged(NoteResultsMode.FavoriteNotes))
            },
        ),
        ActionNoteData(
            text = "Your trash bin",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFFE15A51),
            resultMode = NoteResultsMode.TrashNotes,
            onClick = {
                notesViewModel.onEvent(NotesEvent.ResultsChanged(NoteResultsMode.TrashNotes))
            },
        )
    )

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 120.dp)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // Get the screen width in dp
    val availableWidth = screenWidth - (16.dp * 2) // Subtract the left and right padding
    val numColumns = (availableWidth / 128.dp).toInt() // Calculate the number of available columns

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        item(span = { GridItemSpan(numColumns) }) {
            HeaderNoteList()
        }

        itemsIndexed(actionNotesList) { _, dataAction ->
            ActionNote(
                text = dataAction.text,
                leadingIcon = dataAction.leadingIcon,
                colorIcon = dataAction.colorIcon,
                onClick = dataAction.onClick,
                isSelected = notesViewModel.resultsMode.value == dataAction.resultMode
            )
        }

        item(span = { GridItemSpan(numColumns) }) {
            FolderList(app, notesViewModel, folderViewModel)
        }

        item(span = { GridItemSpan(numColumns) }) {
            val message = when (notesViewModel.resultsMode.value) {
                is NoteResultsMode.AllNotes -> {
                    CustomMessageData(
                        title = "Recent notes",
                        subTitle = "Keep your ideas at hand"
                    )
                }

                is NoteResultsMode.TrashNotes -> {
                    CustomMessageData(
                        title = "Trashed notes",
                        subTitle = ""
                    )
                }

                is NoteResultsMode.FolderNotes -> {
                    val selectedFolder =
                        notesViewModel.state.value.selectedFolder?.name ?: "selected"
                    CustomMessageData(
                        title = "Folder notes",
                        subTitle = "Notes in $selectedFolder folder"
                    )
                }

                is NoteResultsMode.FavoriteNotes -> {
                    CustomMessageData(
                        title = "Favorite notes",
                        subTitle = "Your favorite notes"
                    )
                }
            }
            TitleNoteList(message.title, message.subTitle)
        }

        item(span = { GridItemSpan(numColumns) }) {
            ShortNoteItems(
                noteOrder = state.noteOrder
            ) {
                notesViewModel.onEvent(NotesEvent.Order(it))
            }
        }

        val isLoading = notesViewModel.state.value.isLoading

        if (isLoading) {
            val array = arrayOf(1, 2, 3, 4)
            items(array) {
                NoteItemContainer(
                    message = "",
                    title = "Lorem Ipsum",
                    updatedAt = Date(),
                    theme = Color.Transparent,
                    onNoteClick = {},
                    onLongNoteClick = {},
                    modifier = Modifier.shimmerEffect(),
                    cardState = CardState.LOADING,
                )
            }
        } else {
            val notes = notesViewModel.state.value.notes
            if (notes.isEmpty()) {
                item(span = { GridItemSpan(numColumns) }) {
                    val noNotesMessage = when (notesViewModel.resultsMode.value) {
                        is NoteResultsMode.AllNotes -> "You haven't created any notes yet. Start creating your notes now."
                        is NoteResultsMode.TrashNotes -> "You don't have any notes in the recycle bin."
                        is NoteResultsMode.FolderNotes -> {
                            val selectedFolder =
                                notesViewModel.state.value.selectedFolder?.name ?: "selected"
                            "No notes in $selectedFolder folder."
                        }

                        is NoteResultsMode.FavoriteNotes -> "You don't have any favorite notes yet. Mark some notes as favorites."
                    }
                    Box(modifier = Modifier.padding(24.dp)) {
                        Text(
                            noNotesMessage,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            } else {
                itemsIndexed(notes) { _, dataNote ->
                    val noteColor = Color(android.graphics.Color.parseColor(dataNote.theme))
                    val resNoteColor = noteColor.takeUnless { it == Color.White }
                        ?: MaterialTheme.colorScheme.primaryContainer

                    //Selection mode and states
                    val isSelected = dataNote.id == selectedCard.value?.id

                    NoteItemContainer(
                        message = dataNote.message,
                        title = dataNote.title,
                        updatedAt = dataNote.updatedAt,
                        theme = resNoteColor,
                        cardState = if (isSelected) CardState.SELECTED else
                            if (isSelectionMode.value) CardState.DESELECTED
                            else CardState.NORMAL,
                        onLongNoteClick = {
                            selectedCard.value = dataNote
                            isSelectionMode.value = true
                            onLongNoteClick()
                        },
                    ) {
                        //If not in selection mode, navigate to the item
                        if (!isSelectionMode.value) {
                            val folderId = notesViewModel.state.value.selectedFolder?.id ?: ""
                            val isReadMode =
                                notesViewModel.resultsMode.value == NoteResultsMode.TrashNotes

                            navController.navigate(
                                Screen.Note.route +
                                        "?noteId=${dataNote.id}&noteColor=${noteColor.toArgb()}&folderId=$folderId&isReadMode=${isReadMode}"
                            )
                        } else {
                            selectedCard.value = dataNote
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying the header of the note list.
 * It shows the title "Your notes" along with the number of notes.
 * The number of notes is retrieved from the API.
 */
@Composable
fun HeaderNoteList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 32.dp)
    ) {

        //TODO Get the number of notes from API
        Title(
            message = CustomMessageData(
                title = "Your notes",
                subTitle = "Organize and manage your notes with ease"
            ),
            alignment = Alignment.CenterHorizontally
        )
    }
}

/**
 * Composable function for displaying the title of the note list.
 * It shows the title "Recent notes" along with a subtitle.
 */
@Composable
fun TitleNoteList(
    title: String,
    subTitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Title(
            message = CustomMessageData(
                title = title,
                subTitle = subTitle
            ),
            titleStyle = MaterialTheme.typography.headlineSmall
        )
    }
}

/**
 * Composable function for displaying a note item container.
 *
 * @param message The message of the note.
 * @param title The title of the note.
 * @param updatedAt The date when the note was last updated.
 * @param theme The theme color of the note.
 * @param onNoteClick The callback function for when the note item is clicked.
 */
@Composable
fun NoteItemContainer(
    message: String,
    title: String,
    updatedAt: Date,
    modifier: Modifier = Modifier,
    theme: Color = Color.White,
    cardState: CardState,
    onLongNoteClick: () -> Unit,
    onNoteClick: () -> Unit,
) {
    val textColor =
        if (cardState == CardState.LOADING) Color.Transparent else MaterialTheme.colorScheme.onPrimary

    Column {
        NoteItem(
            text = message,
            color = theme,
            onClick = onNoteClick,
            onLongClick = onLongNoteClick,
            modifier = if (cardState == CardState.LOADING) Modifier.shimmerEffect(12.dp) else modifier,
            cardState = cardState,
        )

        Title(
            message = CustomMessageData(
                title = title,
                subTitle = DateUtils.formatDateToString(updatedAt)
            ),
            alignment = Alignment.CenterHorizontally,
            titleStyle = TextStyle(
                color = textColor,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            ),
            subtitleStyle =
            TextStyle(
                color = textColor,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
            ),
            modifier = if (cardState == CardState.LOADING) Modifier
                .shimmerEffect() else Modifier,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ShortNoteItems(
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
) {
    val rotate = if (noteOrder.orderType is OrderType.Ascending) {
        180f
    } else {
        0f
    }

    Box(
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onOrderChange(
                    noteOrder.copy(
                        if (noteOrder.orderType is OrderType.Ascending) {
                            OrderType.Descending
                        } else {
                            OrderType.Ascending
                        }
                    )
                )
            },
        ) {
            Text(
                text = "Last Modification",
                modifier = Modifier
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(0.8f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
                    .rotate(rotate)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FolderList(
    app: Application,
    notesViewModel: NotesViewModel,
    folderViewModel: FolderViewModel
) {
    var openFolderBottomSheet by rememberSaveable { mutableStateOf(false) }
    BottomSheetLayout(
        openBottomSheet = openFolderBottomSheet,
        onDismissRequest = { openFolderBottomSheet = false },
        content = {
            FolderBottomSheetContent(
                onBottomSheetChanged = { newValue -> openFolderBottomSheet = newValue },
                folderViewModel = folderViewModel,
            )
        },
    )

    AnimatedVisibility(
        visible = app.getRoles()
            .contains("premium") && notesViewModel.resultsMode.value == NoteResultsMode.FolderNotes,
    ) {
        Column {
            TitleNoteList("Your Folders", "Organize your files efficiently")

            if (notesViewModel.state.value.folders.isEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        "You don't have any folders yet. ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Create one here.",
                        modifier = Modifier.clickable { openFolderBottomSheet = true },
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    notesViewModel.state.value.folders.forEach { folder ->
                        FolderItem(
                            folder,
                            colorBackgroundFolder = Color(android.graphics.Color.parseColor(folder.theme))
                        ) {
                            notesViewModel.onEvent(
                                NotesEvent.SelectedFolderChanged(
                                    folder
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            openFolderBottomSheet = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                        )
                    }
                }
            }
        }
    }
}