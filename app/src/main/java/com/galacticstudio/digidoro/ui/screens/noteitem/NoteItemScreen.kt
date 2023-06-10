package com.galacticstudio.digidoro.ui.screens.noteitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.tagList
import com.galacticstudio.digidoro.ui.screens.noteitem.components.DottedDivider
import com.galacticstudio.digidoro.ui.screens.noteitem.components.TagItem
import com.galacticstudio.digidoro.ui.screens.noteitem.components.TransparentTextField
import com.galacticstudio.digidoro.ui.shared.floatingCards.BottomSheetLayout
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.ui.shared.textfield.SearchBarItem
import kotlinx.coroutines.CoroutineScope

/**
 * A composable function used for previewing a note item.
 */
@Preview(showSystemUi = true)
@Composable
fun NoteItemScreenPreview() {
    val navController = rememberNavController()
    NoteItemScreen(navController = navController, noteColor = Color.White)
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
) {
    TopBarNote(
        navController = navController,
        content = {
            NoteItemContent(noteColor = noteColor)
        },
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .wrapContentSize(Alignment.Center)
    ) {
        var text by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(noteColor),
        ) {
            TransparentTextField(
                text = text,
                hint = "Hint",
                modifier = Modifier.padding(36.dp),
                applyFillMaxHeight = true,
                onValueChange = { text = it },
                onFocusChange = { },
                textStyle = MaterialTheme.typography.bodyLarge,
                hintStyle = MaterialTheme.typography.bodyLarge,
                hintColor = Color.Black.copy(alpha = 0.45f),
            )
        }
    }
}

/**
 * A composable function representing the top bar of a note.
 *
 * @param navController The NavController used for navigation.
 * @param content The content of the note item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNote(
    navController: NavController,
    content: @Composable () -> Unit = {},
) {
    var title by remember { mutableStateOf("") }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TransparentTextField(
                        text = title,
                        hint = "Change Title",
                        onValueChange = { title = it },
                        onFocusChange = {
                            /* TODO */
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge,
                        hintStyle = MaterialTheme.typography.titleLarge,
                        hintColor = Color.Black.copy(alpha = 0.45f),
                        fontWeight = FontWeight.W700,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                    DropDownNoteMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(100f)
                    .background(if (showMenu) Color.Black.copy(alpha = 0.4f) else Color.Transparent)
            ) {
                content()
            }
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
) {
    var openTagBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openFolderBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openColorBottomSheet by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp),
    ) {
        Text(
            text = "Configuration",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W800
        )
        DottedDivider(color = Color(0xFF313131))
        DropdownMenuItem(
            text = { Text("Folders") },
            onClick = { openFolderBottomSheet = true }
        )
        DropdownMenuItem(
            text = { Text("Tags") },
            onClick = { openTagBottomSheet = true }
        )
        DropdownMenuItem(
            text = { Text("Colors") },
            onClick = { openColorBottomSheet = true }
        )
        DottedDivider(color = Color(0xFF313131))
        Row(
            Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropdownMenuItem(
                modifier = Modifier.width(50.dp),
                text = {
                    //TODO Implement the favorite action
                    Icon(
                        painter = painterResource(R.drawable.star_outline_icon),
                        contentDescription = "Favorite Note",
                        modifier = Modifier.size(25.dp)
                    )
                },
                onClick = { /*TODO*/ }
            )
            Spacer(modifier = Modifier.width(75.dp))
            DropdownMenuItem(
                modifier = Modifier.width(50.dp),
                text = {
                    //TODO Implement the favorite action
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "Delete Note",
                        modifier = Modifier.size(25.dp),
                    )
                },
                onClick = { /*TODO*/ }
            )
        }
    }
    BottomSheetLayout(
        openBottomSheet = openTagBottomSheet,
        onDismissRequest = { openTagBottomSheet = false },
        content = { scope: CoroutineScope -> TagBottomSheetContent(scope) }
    )
    BottomSheetLayout(
        openBottomSheet = openFolderBottomSheet,
        onDismissRequest = { openFolderBottomSheet = false },
        content = { scope: CoroutineScope -> FolderBottomSheetContent(scope) }
    )
    BottomSheetLayout(
        openBottomSheet = openColorBottomSheet,
        onDismissRequest = { openColorBottomSheet = false },
        content = { scope: CoroutineScope -> ColorBottomSheetContent(scope) }
    )
}

/**
 * A composable function representing the content of the tag bottom sheet.
 *
 * @param scope The coroutine scope.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagBottomSheetContent(
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Add Tags",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.clickable {
                    /* TODO */
                },
                text = "Save",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W800,
            )
        }

        FlowRow(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tagList.forEach { tag ->
                TagItem(
                    name = tag.name,
                    onClick = { /* TODO Remove Tag from tag list */ }
                )
            }
        }

        var search by remember { mutableStateOf("") }
        SearchBarItem(
            search = search,
            hintText = "Seach",
            onValueChange = { search = it },
            onSearchClick = { /* TODO */ }
        )
    }
}

/**
 * A composable function representing the content of the folder bottom sheet.
 *
 * @param scope The coroutine scope.
 */
@Composable
fun FolderBottomSheetContent(
    scope: CoroutineScope
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = "Folder Name",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Placeholder") },
            shape = RectangleShape,
            singleLine = true,

        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W800,
                )
            }
        }
    }
}

/**
 * A composable function representing the content of the color bottom sheet.
 *
 * @param scope The coroutine scope.
 */
@Composable
fun ColorBottomSheetContent(
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Folder Color",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ColorBox()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W800,
                )
            }
        }
    }
}