package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying action notes.
 *
 * @param text The text to display.
 * @param leadingIcon The optional leading icon.
 * @param colorIcon The color of the leading icon.
 * @param onClick The callback function for when the action note is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionNote(
    text: String,
    leadingIcon: Painter? = null,
    colorIcon: Color = Color.Magenta,
    onClick: () -> Unit,
    isSelected: Boolean,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(0.5f)
                else MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 9.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingIcon?.let { icon ->
                Row(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(colorIcon),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color.White
                    )
                }

            }
            Text(
                text = text,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}