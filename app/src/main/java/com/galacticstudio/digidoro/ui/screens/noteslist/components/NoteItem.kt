package com.galacticstudio.digidoro.ui.screens.noteslist.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.screens.noteslist.CardState
import com.galacticstudio.digidoro.util.ColorCustomUtils
import com.galacticstudio.digidoro.util.ColorCustomUtils.Companion.adjustColorBrightness

/**
 * Composable function for displaying short note items.
 *
 * @param text The text of the note item.
 * @param color The color of the note item.
 * @param onClick The callback function for when the note item is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    modifier: Modifier,
    cardState: CardState,
) {
    val textColor = ColorCustomUtils.returnLuminanceColor(color)

    val luminanceColor = if (cardState == CardState.DESELECTED) {
        if (isSystemInDarkTheme()) 0f else 0f
    } else if (cardState == CardState.SELECTED) {
        if (isSystemInDarkTheme()) 0.5f else 0.2f
    } else {
        1f
    }
    val selectedColor = adjustColorBrightness(
        color,
        !isSystemInDarkTheme(),
        luminanceColor
    )

    val elevation = if (cardState == CardState.LOADING) {
        CardDefaults.cardElevation()
    } else {
        CardDefaults.cardElevation(
            defaultElevation = 6.5.dp,
            pressedElevation = 6.5.dp,
            focusedElevation = 6.5.dp,
            hoveredElevation = 6.5.dp,
        )
    }

    Box {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (cardState == CardState.NORMAL) color else selectedColor,
            ),
            modifier = Modifier
                .height(200.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .then(modifier)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { onLongClick?.invoke() },
                ),
            elevation = elevation,
            shape = MaterialTheme.shapes.medium,
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
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                )
            }
        }

        val buttonSize = 36.dp
        Box(modifier = Modifier.padding(15.dp)) {
            if (cardState == CardState.SELECTED) {
                Icon(
                    painter = painterResource(R.drawable.checkbox_marked_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
                    modifier = Modifier.size(buttonSize)
                )
            } else if (cardState == CardState.DESELECTED) {
                Icon(
                    painter = painterResource(R.drawable.checkbox_outline_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
                    modifier = Modifier.size(buttonSize)
                )
            }
        }
    }
}
