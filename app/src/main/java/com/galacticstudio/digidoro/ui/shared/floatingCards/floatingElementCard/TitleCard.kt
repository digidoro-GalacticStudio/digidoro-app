package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleCard(
    value: String = "",
    placeHolder: String
){
    var value by remember { mutableStateOf(value) }
    DigidoroTheme {
        Box {
            TextField(
                value = value,
                onValueChange = { value = it },
                textStyle = MaterialTheme.typography.headlineMedium + TextStyle( fontWeight = FontWeight.ExtraBold ),
                placeholder = { Text(placeHolder, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold) },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
//                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
//                    placeholderColor = colorResource(id = R.color.black_text_color),
//                    textColor = colorResource(id = R.color.black_text_color)
                ),
            )

        }
    }
}