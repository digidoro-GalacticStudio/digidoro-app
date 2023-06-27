package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.tagList
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemEvent
import com.galacticstudio.digidoro.ui.screens.noteitem.viewmodel.NoteItemViewModel
import com.galacticstudio.digidoro.ui.shared.textfield.SearchBarItem

/**
 * A composable function representing the content of the tag bottom sheet.
 *
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagBottomSheetContent(
    noteItemViewModel: NoteItemViewModel,
    openTagBottomSheet: MutableState<Boolean>,
) {
    val state = noteItemViewModel.state.value

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
                    openTagBottomSheet.value = false
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
            state.tags.forEach { tag ->
                TagItem(
                    name = tag,
                    onDeleteClick = {
                        noteItemViewModel.onEvent(NoteItemEvent.TagsChanged(
                            tags = state.tags.filterNot { it == tag }
                        ))
                    }
                )
            }
        }

        var search by remember { mutableStateOf("") }
        SearchBarItem(
            search = search,
            hintText = "Seach",
            onValueChange = { search = it },
            onSearchClick = {
                noteItemViewModel.onEvent(NoteItemEvent.TagsChanged(
                    tags = state.tags + search
                ))
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}
