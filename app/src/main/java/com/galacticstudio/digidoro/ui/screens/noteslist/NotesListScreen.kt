package com.galacticstudio.digidoro.ui.screens.noteslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.itemNotesList
import com.galacticstudio.digidoro.navigation.AppScaffold
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.noteslist.components.ActionNote
import com.galacticstudio.digidoro.ui.screens.noteslist.components.NoteItem
import com.galacticstudio.digidoro.ui.shared.textfield.SearchBarItem
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.util.DateUtils
import java.util.Date

/**
 * Preview function for the notes list screen..
 */
@Preview(showSystemUi = true)
@Composable
fun NotesListSPreview() {
    val navController = rememberNavController()
    NotesListScreen(navController = navController)
}

data class ActionNoteData(
    val text: String,
    val leadingIcon: Painter,
    val colorIcon: Color,
    val onClick: () -> Unit
)

/**
 * A composable function representing the note list screen.
 *
 * @param navController The navigation controller used for navigating to different screens.
 */
@Composable
fun NotesListScreen(
    navController: NavHostController
) {
    AppScaffold(
        navController = navController,
        content = {
            NoteListContent(navController = navController)
        }
    )
}

/**
 * Composable function for displaying the content of the note list screen.
 *
 * @param navController The navigation controller used for navigating to other screens.
 */
@Composable
fun NoteListContent(navController: NavHostController) {
    val actionNotesList = listOf(
        ActionNoteData(
            text = "All notes",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFF4880FF),
            onClick = {}
        ),
        ActionNoteData(
            text = "Your folders",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFF202124),
            onClick = {}
        ),
        ActionNoteData(
            text = "Your favorites",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFFFFC700),
            onClick = {}
        ),
        ActionNoteData(
            text = "Your trash bin",
            leadingIcon = painterResource(R.drawable.email_icon),
            colorIcon = Color(0xFFE15A51),
            onClick = {}
        )
    )

    var search by remember { mutableStateOf("") }

    val cols = 2
    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(35.dp),
    ) {
        item(span = { GridItemSpan(cols) }) {
            HeaderNoteList()
        }

        item(span = { GridItemSpan(cols) }) {
            Box(modifier =Modifier.padding(bottom = 32.dp)) {
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
                onClick = dataAction.onClick
            )
        }

        item(span = { GridItemSpan(cols) }) {
            TitleNoteList()
        }

        item(span = { GridItemSpan(cols) }) {
            ShortNoteItems(
                text = "Last Modification",
                icon = painterResource(R.drawable.last_modification_icon)
            ) {
                //TODO Implement this action
            }
        }

        itemsIndexed(itemNotesList) { _, dataNote ->
            val colorNote = Color(android.graphics.Color.parseColor(dataNote.theme))

            NoteItemContainer(
               message = dataNote.message,
                title = dataNote.title,
                updatedAt = dataNote.updatedAt,
                theme = colorNote,
            ) {
                navController.navigate(
                    Screen.Note.route +
                            "?noteId=${dataNote.user_id}&noteColor=${colorNote}"
                )
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
    theme: Color = Color.White,
    onNoteClick: () -> Unit,
) {
    Column {
        NoteItem(
            text = message,
            color = theme,
            onClick = onNoteClick,
        )
        Title(
            message = CustomMessageData(
                title = title,
                subTitle = DateUtils.formatDateToString(updatedAt)
            ),
            alignment = Alignment.CenterHorizontally,
            titleStyle = MaterialTheme.typography.bodyMedium,
            subtitleStyle = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ShortNoteItems(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onClick()
            },
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7A7A7A)
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