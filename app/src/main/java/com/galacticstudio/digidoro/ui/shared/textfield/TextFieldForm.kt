package com.galacticstudio.digidoro.ui.shared.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Nunito

@Composable
fun TextFieldForm(
    label: String,
    placeholder: String,
    type: TextFieldType = TextFieldType.TEXT,
) {
    Column {
        Text(
            text = label,
            fontSize = 18.sp,
            fontFamily = Nunito,
            fontWeight = FontWeight.W800,
            lineHeight = 25.sp,
            color = Color(0xFF202124),
            style = TextStyle(
                letterSpacing = 0.02.sp
            )
        )
        Spacer(modifier = Modifier.height( 6.dp ))
        TextFieldItem(
            placeholder = placeholder,
            type = type,
        )
    }
}
