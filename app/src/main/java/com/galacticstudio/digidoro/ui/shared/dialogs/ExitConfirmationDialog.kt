package com.galacticstudio.digidoro.ui.shared.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun ExitConfirmationDialog(
    title: String,
    text: String,
    openDialog: MutableState<Boolean>,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = text)
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmClick() }
                ) {
                    Text("Confirm", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissClick() }
                ) {
                    Text("Dismiss", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            textContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}