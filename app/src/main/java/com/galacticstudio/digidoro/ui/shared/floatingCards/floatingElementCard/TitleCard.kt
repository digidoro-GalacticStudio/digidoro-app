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
import androidx.compose.ui.tooling.preview.Preview
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
/**
 * Function that works us Title field for elements created in the app
 * @param placeHolder string that work us place holder of the element
 * @param value intial value the field will have with default value as ""
 */
@Composable
fun TitleCard(
    placeHolder: String,
    value: String = "",
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
                    unfocusedIndicatorColor = Color.Transparent ,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
            )

        }
    }
}