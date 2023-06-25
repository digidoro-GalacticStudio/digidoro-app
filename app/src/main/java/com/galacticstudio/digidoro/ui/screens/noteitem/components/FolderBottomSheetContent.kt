package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.FolderEvent
import com.galacticstudio.digidoro.ui.screens.noteslist.viewmodel.FolderViewModel
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.util.ColorCustomUtils

/**
 * A composable function representing the content of the folder bottom sheet.
 *
 */
@Composable
fun FolderBottomSheetContent(
    folderViewModel: FolderViewModel,
    onBottomSheetChanged: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        val state = folderViewModel.state
        TextFieldForm(
            label = "Folder Name",
            placeholder = "Your folder name",
            type = TextFieldType.TEXT,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.folder_icon),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            },
            value = state.value.name,
            isError = state.value.nameError != null,
        ) {
            folderViewModel.onEvent(FolderEvent.NameChanged(it))
        }

        ErrorMessage(state.value.nameError)

        Spacer(modifier = Modifier.height(32.dp))
        DottedDivider(color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
        Spacer(modifier = Modifier.height(16.dp))

        ColorBox(
            selectedColor = Color(android.graphics.Color.parseColor("#${folderViewModel.noteColor.value}")),
            onColorChange = {
                folderViewModel.onEvent(
                    FolderEvent.ColorChanged(
                        ColorCustomUtils.convertColorToString(it)
                    )
                )
            },
            isUserPremium = true,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBottomSheetChanged(false) }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
            TextButton(
                onClick = {
                    folderViewModel.onEvent(FolderEvent.SaveFolder)
                    onBottomSheetChanged(false)
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