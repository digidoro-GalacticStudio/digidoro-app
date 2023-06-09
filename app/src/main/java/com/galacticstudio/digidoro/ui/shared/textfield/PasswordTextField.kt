package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.util.shadowWithBorder
import com.galacticstudio.digidoro.ui.theme.Gray60
import com.galacticstudio.digidoro.util.WindowSize

/**
 * A composable function that represents a password text field with an optional leading icon.
 *
 * @param value The current value of the password text field.
 * @param placeholder The placeholder text to be displayed when the password text field is empty.
 * @param leadingIcon The leading icon to be displayed before the password text field. (optional)
 * @param modifier The modifier to be applied to the password text field.
 * @param isError Whether the password text field should display an error state.
 * @param onTextFieldChanged The callback function to be called when the password text field value changes.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(
    value: String,
    placeholder: String,
    leadingIcon: Painter?,
    modifier: Modifier = Modifier,
    isError: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
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
                        else MaterialTheme.colorScheme.onPrimary,
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
                focusedLeadingIconColor = Gray60,
                unfocusedLeadingIconColor = Gray60,
                errorLeadingIconColor = MaterialTheme.colorScheme.error.copy(0.7f),
                disabledPlaceholderColor = Gray60.copy(0.8f),
                focusedTrailingIconColor = Gray60,
                unfocusedTrailingIconColor = Gray60,
                errorTrailingIconColor = MaterialTheme.colorScheme.error.copy(0.7f),
                focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
            ),
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleSmall,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }, trailingIcon = {
                val colorFilter = if (isError) {
                    ColorFilter.tint(MaterialTheme.colorScheme.error)
                } else {
                    ColorFilter.tint(Gray60)
                }

                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Image(
                            painter = painterResource(R.drawable.visibility_off_icon),
                            contentDescription = null,
                            colorFilter = colorFilter,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword = true }) {
                        Image(
                            painter = painterResource(R.drawable.visibility_icom),
                            contentDescription = null,
                            colorFilter = colorFilter,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        )
    }
}
