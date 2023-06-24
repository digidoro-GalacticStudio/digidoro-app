package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

val buttonSize = 28.dp

@Composable
fun ColorBox() {
    DigidoroTheme() {
        Row {
            ColorButton(
                color = colorResource(id = R.color.secondary_color),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.cherry_red_accent),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.mint_accent),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.azure_light_blue_accent),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.honey_yellow_accent),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.lavender_accent),
                onClick = { /*todo*/ })
            ColorButton(
                color = colorResource(id = R.color.gray_text_color),
                onClick = { /*todo*/ })
        }
    }
}


@Composable
fun ColorButton(color: Color, onClick: () -> Unit) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        modifier = Modifier
            .padding(3.dp)
            .padding(end = 5.dp)
            .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            .size(buttonSize)

    ) {

    }

}