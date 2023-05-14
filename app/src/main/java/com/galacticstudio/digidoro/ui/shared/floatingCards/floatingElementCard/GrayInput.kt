package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme


enum class TextFieldType {
    DATE,
    NUMBER,
}

val roundCorner = 5.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrayInput(
    label: String,
    fieldWidth: Dp = 130.dp,
    placeHolder: String,
    value: String = "",
    type: TextFieldType = TextFieldType.NUMBER,
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
                        modifier = Modifier
                            .width(fieldWidth)
                            .border(
                                1.dp,
                                colorResource(id = R.color.secondary_color),
                                RoundedCornerShape(roundCorner)
                            ),
                        shape = RoundedCornerShape(roundCorner),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorResource(id = R.color.gray_text_color),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            placeholderColor = colorResource(id = R.color.lavender_accent)
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