package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.FolderModel
import com.galacticstudio.digidoro.data.convertToFolderPopulatedModel
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesEvent
import com.galacticstudio.digidoro.ui.screens.noteslist.NotesState
import com.galacticstudio.digidoro.ui.screens.noteslist.components.FolderItem
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.NotesViewModel

@Composable
fun MoveToFolderBottomSheet(
    notesViewModel: NotesViewModel,
    openMoveToFolderDialog: MutableState<Boolean>,
) {
    val emptyFolderModel = FolderModel(
        id = "-1",
        userId = "",
        name = "No folder",
        theme = "#FFFFFF",
        notesId = emptyList(),
        createdAt = "",
        updatedAt = ""
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val state = notesViewModel.state

        Text(
            text = "Actual Folder:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.W800
        )

        AnimatedVisibility(state.value.actualFolder != null) {
            val folderPopulated = convertToFolderPopulatedModel(state.value.actualFolder!!)
            FolderItem(
                folder = folderPopulated,
                colorBackgroundFolder = Color(android.graphics.Color.parseColor(folderPopulated.theme)),
            ) { }
        }

        if (state.value.actualFolder == null) {
            Text(
                text = "No folder.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select destination folder",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.W800
        )
        Spacer(modifier = Modifier.height(16.dp))

        val selectedFolder = remember { mutableStateOf(emptyFolderModel) }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            ExposedDropdownMenuSample(
                emptyFolderModel = emptyFolderModel,
                selectedFolder = selectedFolder,
                notesViewModel = notesViewModel,
                state = state
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { openMoveToFolderDialog.value = false }
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
            TextButton(
                onClick = {
                    notesViewModel.onEvent(NotesEvent.MoveToAnotherFolder)
                    openMoveToFolderDialog.value = false
                }
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W800,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuSample(
    selectedFolder: MutableState<FolderModel>,
    notesViewModel: NotesViewModel,
    state: State<NotesState>,
    emptyFolderModel: FolderModel,
) {
    ListItem(
        headlineContent = {
            Text(
                notesViewModel.state.value.newSelectedFolder?.name ?: "No folder",
                color = MaterialTheme.colorScheme.onPrimary
            )

        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.1f),
        )
    )
    DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))

    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.onSecondaryContainer.copy(0.1f))
    ) {

        item {
            DropdownMenuItem(
                text = { Text(emptyFolderModel.name, color = MaterialTheme.colorScheme.onPrimary) },
                onClick = {
                    selectedFolder.value = emptyFolderModel
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(45.dp)
                            .background(
                                MaterialTheme.colorScheme.onPrimary.copy(0.13f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Red,
                        )
                    }
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }

        itemsIndexed(state.value.newFolderList) { _, folder ->
            DropdownMenuItem(
                text = { Text(folder.name, color = MaterialTheme.colorScheme.onPrimary) },
                onClick = {
                    selectedFolder.value = folder
                    notesViewModel.onEvent(NotesEvent.NewFolderNoteChanged(folder))
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(50.dp)
                            .background(
                                MaterialTheme.colorScheme.onPrimary.copy(0.13f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.folder_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color(android.graphics.Color.parseColor(folder.theme)),
                        )
                    }
                },
                trailingIcon = {
                    Text(
                        "notes: ${folder.notesId.size}",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }
    }
}