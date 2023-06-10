package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A composable function representing a search bar item.
 *
 * @param search The current search text.
 * @param hintText The hint text to display when the search bar is empty.
 * @param modifier The modifier for the search bar.
 * @param onValueChange The callback function invoked when the search text changes.
 * @param onSearchClick The callback function invoked when the search button is clicked.
 */
@Composable
fun SearchBarItem(
    search: String,
    hintText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .border(1.dp, Color.Black, shape = CircleShape)
    ) {
        BasicTextField(
            value = search,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(
                    1.dp,
                    Color.Gray,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(Color.White),
            textStyle = MaterialTheme.typography.bodySmall,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                        end = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        innerTextField()
                        if (search.isEmpty()) {
                            Text(
                                text = hintText,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}