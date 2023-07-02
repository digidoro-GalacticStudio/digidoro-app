package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * A composable function representing a transparent hint text field.
 *
 * @param text The current text value of the field.
 * @param hint The hint text to be displayed when the field is empty.
 * @param modifier The modifier for the text field.
 * @param applyFillMaxHeight Whether to apply the fillMaxHeight modifier to the text field.
 * @param onValueChange The callback for when the value of the field changes.
 * @param textStyle The style for the text in the field.
 * @param hintStyle The style for the hint text.
 * @param hintColor The color of the hint text.
 * @param fontWeight The weight of the text.
 * @param singleLine Whether the text field should be limited to a single line.
 * @param onFocusChange The callback for when the focus state of the field changes.
 */
@Composable
fun TransparentTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    applyFillMaxHeight: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    hintStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    hintColor: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.W500,
    singleLine: Boolean = false,
    onFocusChange: ((FocusState) -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .run {
                    if (applyFillMaxHeight) {
                        fillMaxHeight()
                    } else {
                        this
                    }
                }
                .onFocusChanged {
                    if (onFocusChange != null) {
                        onFocusChange(it)
                    }
                },
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        style = hintStyle,
                        fontWeight = fontWeight,
                        color = hintColor,
                    )
                }
                innerTextField()
            }
        )
    }
}