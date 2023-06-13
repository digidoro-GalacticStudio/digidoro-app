package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme


enum class TextFieldType {
    DATE,
    NUMBER,
}

val roundCorner = 5.dp

@Preview
@Composable
fun PreviewGrayInput(){
    GrayInput(label = "some", placeHolder = "place")
}

/**
 * Function that works as composable text field to create elements
 * @param label label de field will have
 * @param placeHolder place holder element will have
 * @param modifier some previous modifier the field will applied
 * @param fieldWidth width from field which has default 130 dp
 * @param value initial value from field blank as default
 * @param type field type for field type Number as default
 * @param selectable indicates text field will or not be salectable
 */
@Composable
fun GrayInput(
    label: String,
    placeHolder: String,
    modifier: Modifier = Modifier,

    fieldWidth: Dp = 130.dp,
    value: String = "",
    type: TextFieldType = TextFieldType.NUMBER
){
    var value by remember { mutableStateOf(value) }

    DigidoroTheme {
        Box {
            Column {
                Text(text = label, style = MaterialTheme.typography.titleLarge)
                Box(modifier = Modifier.height(45.dp)) {
                    TextField(
                        value = value,
                        onValueChange = { value = it },
                        textStyle = MaterialTheme.typography.labelSmall + TextStyle( textAlign = TextAlign.End) ,
                        placeholder = {
                            Text(
                                placeHolder,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        modifier = modifier
                            .width(fieldWidth)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(roundCorner)
                            )
                        ,
                        shape = RoundedCornerShape(roundCorner),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        keyboardOptions = when (type) {
                            TextFieldType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
                            TextFieldType.DATE -> KeyboardOptions(keyboardType = KeyboardType.Number)
                            else -> KeyboardOptions(keyboardType = KeyboardType.Number)
                        },
                        maxLines = 1

                    )

                }
            }
        }
    }
}