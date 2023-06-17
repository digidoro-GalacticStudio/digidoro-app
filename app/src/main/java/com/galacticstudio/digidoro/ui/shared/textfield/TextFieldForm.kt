package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.ui.theme.Nunito

/**
 * A composable function that represents a form field with a label and a text field.
 *
 * @param label The label text to be displayed above the text field.
 * @param placeholder The placeholder text to be displayed when the text field is empty.
 * @param type The type of the text field.
 * @param leadingIcon The leading icon to be displayed in the text field.
 * @param isPassword Determines whether the text field should be a password field.
 */
@Composable
fun TextFieldForm(
    value: String,
    label: String,
    placeholder: String,
    type: TextFieldType = TextFieldType.TEXT,
    leadingIcon: Painter? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    onTextFieldChanged: (String) -> Unit,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = Nunito,
            fontWeight = FontWeight.W800,
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (isPassword) {
            PasswordTextField(
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                value = value,
                isError = isError,
                onTextFieldChanged = onTextFieldChanged
            )
        } else {
            TextFieldItem(
                placeholder = placeholder,
                type = type,
                leadingIcon = leadingIcon,
                value = value,
                isError = isError,
                onTextFieldChanged = onTextFieldChanged
            )
        }
    }
}
