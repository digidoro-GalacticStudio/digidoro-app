package com.galacticstudio.digidoro.ui.shared.textfield

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Nunito

enum class TextFieldType {
    TEXT,
    NUMBER,
    PASSWORD,
    PHONE,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldItem(
    placeholder: String,
    type: TextFieldType = TextFieldType.TEXT,
) {
    var value: String by remember { mutableStateOf("") }

    val borderWidth = LocalDensity.current.run { 2.toDp() }
    val borderRadius = LocalDensity.current.run { 18.toDp() }

    Box {
        Box(
            modifier = Modifier
                .offset(5.dp, 5.dp)
                .matchParentSize()
                .background(colorResource(id = R.color.secondary_color), shape = RoundedCornerShape(borderRadius))
        )
        TextField(
            value = value,
            onValueChange = { value = it },
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = borderWidth,
                    color = colorResource(id = R.color.secondary_color),
                    shape = RoundedCornerShape(borderRadius)
                )
                .clip(RoundedCornerShape(borderRadius)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = colorResource(id = R.color.secondary_color)
            ),
            textStyle = TextStyle(
                fontFamily = Nunito,
                fontWeight = FontWeight.W500,
            ),
            keyboardOptions = when (type) {
                TextFieldType.TEXT -> KeyboardOptions(keyboardType = KeyboardType.Text)
                TextFieldType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
                TextFieldType.PASSWORD -> KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                TextFieldType.PHONE -> KeyboardOptions(keyboardType = KeyboardType.Phone)
                else -> KeyboardOptions(keyboardType = KeyboardType.Text)
            }
        )
    }
}