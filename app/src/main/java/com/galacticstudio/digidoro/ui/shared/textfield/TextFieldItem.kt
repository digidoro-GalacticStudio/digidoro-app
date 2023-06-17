package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.util.shadowWithBorder
import com.galacticstudio.digidoro.ui.theme.Gray60

/**
 * An enum class representing the type of a text field.
 */
enum class TextFieldType {
    TEXT, NUMBER, PASSWORD, PHONE,
}

/**
 * A composable function that represents a text field item.
 *
 * @param placeholder The placeholder text to be displayed when the text field is empty.
 * @param modifier The modifier to be applied to the text field item.
 * @param type The type of the text field.
 * @param leadingIcon The leading icon to be displayed in the text field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldItem(
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    type: TextFieldType = TextFieldType.TEXT,
    leadingIcon: Painter? = null,
    isError: Boolean,
    onTextFieldChanged: (String) -> Unit
) {

    val borderWidth = LocalDensity.current.run { 2.toDp() }
    val borderRadius = LocalDensity.current.run { 18.toDp() }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color(0xFF3D3F42),
        backgroundColor = Color(0xFF789DF1),
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
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
                    shadowOffset = Offset(15f, 15f)
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
                cursorColor = Gray60,
                focusedContainerColor = Color.White,
                unfocusedContainerColor= Color.White,
                errorContainerColor = Color.White,
                focusedPlaceholderColor = Gray60,
                unfocusedPlaceholderColor = Gray60,
                errorPlaceholderColor = MaterialTheme.colorScheme.error.copy(0.7f),
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
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleMedium,
            isError = isError,
            keyboardOptions = when (type) {
                TextFieldType.TEXT -> KeyboardOptions(keyboardType = KeyboardType.Text)
                TextFieldType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
                TextFieldType.PASSWORD -> KeyboardOptions(keyboardType = KeyboardType.Password)
                TextFieldType.PHONE -> KeyboardOptions(keyboardType = KeyboardType.Phone)
            }
        )
    }
}
