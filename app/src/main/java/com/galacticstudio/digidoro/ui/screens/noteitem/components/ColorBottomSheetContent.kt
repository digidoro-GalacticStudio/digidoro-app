package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.ui.screens.noteitem.NoteItemEvent
import com.galacticstudio.digidoro.ui.screens.noteitem.viewmodel.NoteItemViewModel
import com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard.ColorBox
import com.galacticstudio.digidoro.util.ColorCustomUtils

/**
 * A composable function representing the content of the color bottom sheet.
 *
 */
@Composable
fun ColorBottomSheetContent(
    noteItemViewModel: NoteItemViewModel,
    onBottomSheetChanged: (Boolean) -> Unit
) {
    val app: Application = LocalContext.current.applicationContext as Application

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Folder Color",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ColorBox(
            selectedColor = Color(android.graphics.Color.parseColor("#${noteItemViewModel.noteColor.value}")),
            onColorChange = {
                noteItemViewModel.onEvent(
                    NoteItemEvent.ColorChanged(
                        ColorCustomUtils.convertColorToString(it)
                    )
                )
            },
            isUserPremium = app.getRoles().contains("premium")
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onBottomSheetChanged(false) }) {
                Text(
                    text = "Exit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W800,
                )
            }
        }
    }
}