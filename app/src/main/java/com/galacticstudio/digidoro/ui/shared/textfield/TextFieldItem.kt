package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.util.shadowWithBorder
import com.galacticstudio.digidoro.ui.theme.Gray60
import com.galacticstudio.digidoro.util.WindowSize

/**
 * An enum class representing the type of a text field.
 */
enum class TextFieldType {
    TEXT, NUMBER, PASSWORD, PHONE,
}

/**
 * A composable function that represents a text field item.
 *
 * @param value The current value of the text field.
 * @param placeholder The placeholder text to be displayed when the text field is empty.
 * @param modifier The modifier to be applied to the text field.
 * @param type The type of the text field. Defaults to [TextFieldType.TEXT].
 * @param leadingIcon The leading icon to be displayed before the text field. (optional)
 * @param trailingIcon The trailing icon composable to be displayed after the text field. (optional)
 * @param isError Whether the text field should display an error state.
 * @param enabled Whether the text field is enabled for user interaction. Defaults to `true`.
 * @param onTextFieldChanged The callback function to be called when the text field value changes.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldItem(
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    type: TextFieldType = TextFieldType.TEXT,
    leadingIcon: Painter? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean,
    enabled: Boolean = true,
    onTextFieldChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val borderWidth = 1.dp
    val borderRadius = 5.dp

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color(0xFF3D3F42),
        backgroundColor = Color(0xFF789DF1),
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        val screenSize = LocalConfiguration.current.screenWidthDp.dp
        val topOffSet = if (screenSize < WindowSize.COMPACT) Offset(15f, 15f) else Offset(12f, 12f)
        TextField(
            value = value,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .shadowWithBorder(
                    borderWidth = 1.dp,
                    borderColor = if (isError) MaterialTheme.colorScheme.error
                        else  MaterialTheme.colorScheme.onPrimary,
                    cornerRadius = borderRadius,
                    shadowColor = if (isError) MaterialTheme.colorScheme.error.copy(0.6f) else Color(0xFF202124),
                    shadowOffset = topOffSet
                )
                .border(
                    width = borderWidth,
                    color = if (isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(borderRadius)
                )
                .then(modifier),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Gray60,
                unfocusedTextColor = Gray60,
                errorTextColor = Gray60,
                disabledTextColor = Gray60.copy(0.8f),
                cursorColor = Gray60,
                focusedContainerColor = Color.White,
                unfocusedContainerColor= Color.White,
                errorContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedPlaceholderColor = Gray60,
                unfocusedPlaceholderColor = Gray60,
                errorPlaceholderColor = MaterialTheme.colorScheme.error.copy(0.7f),
                disabledPlaceholderColor = Gray60.copy(0.8f),
                focusedLeadingIconColor = Gray60,
                unfocusedLeadingIconColor = Gray60,
                errorLeadingIconColor = MaterialTheme.colorScheme.error.copy(0.7f),
                focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
            ),
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        painter = it, contentDescription = null, modifier = Modifier.size(25.dp)
                    )
                }
            },
            trailingIcon = trailingIcon?.let {
                trailingIcon
            },
            enabled = enabled,
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleMedium,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = when (type) {
                    TextFieldType.TEXT -> KeyboardType.Text
                    TextFieldType.NUMBER -> KeyboardType.Number
                    TextFieldType.PASSWORD -> KeyboardType.Password
                    TextFieldType.PHONE -> KeyboardType.Phone
                },
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
    }
}
