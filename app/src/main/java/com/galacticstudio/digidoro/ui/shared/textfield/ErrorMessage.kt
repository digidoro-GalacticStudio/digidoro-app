package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying an error message.
 *
 * @param value The error message to display.
 */
@Composable
fun ErrorMessage (
    value: String?,
) {
    AnimatedVisibility (
        visible = value != null,
    ) {
        Text(
            text = value ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
        )
    }
}