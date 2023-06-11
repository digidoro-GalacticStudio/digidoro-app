package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R

/**
 * A composable function representing a tag item.
 *
 * @param name The name of the tag.
 * @param onClick The callback function invoked when the tag is clicked.
 */
@Composable
fun TagItem(
    name: String,
    onClick: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
        ),
        modifier = Modifier
            .padding(bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            IconButton(
                modifier = Modifier.size(35.dp),
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}