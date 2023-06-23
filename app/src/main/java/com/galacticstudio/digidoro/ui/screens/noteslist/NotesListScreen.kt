package com.galacticstudio.digidoro.ui.screens.noteslist

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.noteslist.components.ActionNote
import com.galacticstudio.digidoro.ui.screens.noteslist.components.NoteItem
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.NotesViewModel
import com.galacticstudio.digidoro.ui.shared.textfield.SearchBarItem
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.DateUtils
import com.galacticstudio.digidoro.util.shimmerEffect
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
            NotesListScreen(navController = navController)
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

/**
 * A composable function representing the note list screen.
 *
 * @param navController The navigation controller used for navigating to different screens.
 */
@Composable
fun NotesListScreen(
    navController: NavHostController,
    notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory),
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val color = Color.White.toArgb()
                    navController.navigate(Screen.Note.route + "?noteId=&noteColor=${color}")
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ad_note_icon),
                    contentDescription = "Add note",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp),
                )
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NotesListContent(
                navController = navController,
                notesViewModel = notesViewModel
            )
        }
    }
}

@Composable
fun NotesListContent(
    navController: NavHostController,
    notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory),
) {
    val state = notesViewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        notesViewModel.onEvent(NotesEvent.Rebuild)
    }

    LaunchedEffect(key1 = context) {
        // Collect the response events from the loginViewModel
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

                is NotesResponseState.Success -> {
                    Toast.makeText(
                        context,
                        "Todas las notas correctas",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
    }

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

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 75.dp)
    val cols = 2

    var search by remember { mutableStateOf("") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        item(span = { GridItemSpan(cols) }) {
            HeaderNoteList()
        }

        item(span = { GridItemSpan(cols) }) {
            Box(modifier = Modifier.padding(bottom = 32.dp)) {
                SearchBarItem(
                    search = search,
                    hintText = "Seach",
                    onValueChange = { search = it },
                    onSearchClick = { /* TODO */ }
                )
            }
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

        item(span = { GridItemSpan(cols) }) {
            TitleNoteList()
        }

        item(span = { GridItemSpan(cols) }) {
            ShortNoteItems(
                icon = painterResource(R.drawable.last_modification_icon),
                noteOrder = state.noteOrder
            ) {
                notesViewModel.onEvent(NotesEvent.Order(it))
            }
        }

        val isLoading = notesViewModel.state.value.isLoading

        if (isLoading) {
            val array = arrayOf(1, 2, 3, 4)

            itemsIndexed(array) { _, value ->
                NoteItemContainer(
                    message = "",
                    title = "Lorem Ipsum",
                    updatedAt = Date(),
                    theme = Color.Transparent,
                    onNoteClick = {},
                    modifier = Modifier
                        .shimmerEffect(),
                    isLoading = true,
                )
            }
        } else {
            itemsIndexed(notesViewModel.state.value.notes) { _, dataNote ->
                val noteColor = Color(android.graphics.Color.parseColor(dataNote.theme))
                val resNoteColor = noteColor.takeUnless { it == Color.White }
                    ?: MaterialTheme.colorScheme.primaryContainer

                NoteItemContainer(
                    message = dataNote.message,
                    title = dataNote.title,
                    updatedAt = dataNote.updatedAt,
                    theme = resNoteColor,
                    isLoading = false,
                ) {
                    navController.navigate(
                        Screen.Note.route +
                                "?noteId=${dataNote.id}&noteColor=${noteColor.toArgb()}"
                    )
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
                subTitle = "## notes"
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
fun TitleNoteList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Title(
            message = CustomMessageData(
                title = "Recent notes",
                subTitle = "Keep your ideas at hand"
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
    isLoading: Boolean,
    theme: Color = Color.White,
    onNoteClick: () -> Unit,
) {
    Column {
        NoteItem(
            text = message,
            color = theme,
            onClick = onNoteClick,
            modifier = if (isLoading) Modifier.shimmerEffect(12.dp) else modifier,
            isLoading = isLoading,
        )
        Title(
            message = CustomMessageData(
                title = title,
                subTitle = DateUtils.formatDateToString(updatedAt)
            ),
            alignment = Alignment.CenterHorizontally,
            titleStyle = TextStyle(
                color = if (isLoading) Color.Transparent else MaterialTheme.colorScheme.onPrimary,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            ),
            subtitleStyle =
            TextStyle(
                color = if (isLoading) Color.Transparent else MaterialTheme.colorScheme.onPrimary,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
            ),
            modifier = modifier,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ShortNoteItems(
    icon: Painter,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
) {
    val text: String = if (noteOrder.orderType is OrderType.Ascending) {
        "First Creation"
    } else {
        "Last Modification"
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
                text = text,
                modifier = Modifier
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(0.8f)
            )
            Icon(
                painter = icon,
                contentDescription = "Icon",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}