package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.dialogs.CustomDatePickerDialog
import com.galacticstudio.digidoro.util.DateUtils

/**
 * Composable function for displaying a date input text field.
 *
 * @param value The current value of the text field.
 * @param error Whether the text field should display an error state.
 * @param onTextFieldChanged The callback invoked when the text field value changes.
 */
@Composable
fun DateInputTextField(
    value: String,
    error: Boolean,
    onTextFieldChanged: (String) -> Unit,
) {
    val openDialog = remember { mutableStateOf(false) }

    TextFieldForm(
        value = value,
        modifier = Modifier.clickable { openDialog.value = true },
        label = "Your birthday",
        placeholder = "mm-DD-yyyy",
        type = TextFieldType.NUMBER,
        leadingIcon = null,
        enabled = false,
        isError = error,
        trailingIcon = {
            IconButton(onClick = { openDialog.value = true }) {
                Image(
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }
            CustomDatePickerDialog(
                openDialog = openDialog,
                onConfirmClick = { date ->
                    onTextFieldChanged(DateUtils.formatLongDateToString(date))
                }
            )
        },
    ) {}
}

