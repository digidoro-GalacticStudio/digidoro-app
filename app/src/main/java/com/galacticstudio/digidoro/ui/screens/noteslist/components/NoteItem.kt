package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying short note items.
 *
 * @param text The text of the note item.
 * @param color The color of the note item.
 * @param onClick The callback function for when the note item is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(200.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.5.dp,
            pressedElevation = 6.5.dp,
            focusedElevation = 6.5.dp,
            hoveredElevation = 6.5.dp,
        ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
