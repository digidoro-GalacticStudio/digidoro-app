package com.galacticstudio.digidoro.ui.shared.floatingCards.floatingElementCard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.ColorCustomUtils.Companion.convertColorToString

val buttonSize = 32.dp

data class ColorItem(val color: Color, val isPremium: Boolean)

@Preview(name = "Full Preview", showSystemUi = true)
@Composable
fun ColorBoxPreview() {
    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ColorBox(Color.White, {})
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorBox(
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    isUserPremium: Boolean = false,
) {

    val colors = listOf(
        ColorItem(colorResource(id = R.color.secondary_color), isPremium = false),
        ColorItem(colorResource(id = R.color.mint_accent), isPremium = false),
        ColorItem(colorResource(id = R.color.azure_light_blue_accent), isPremium = false),
        ColorItem(colorResource(id = R.color.cherry_red_accent), isPremium = true),
        ColorItem(colorResource(id = R.color.honey_yellow_accent), isPremium = true),
        ColorItem(colorResource(id = R.color.lavender_accent), isPremium = true),
        ColorItem(colorResource(id = R.color.gray_text_color), isPremium = true)
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        colors.forEach { colorItem ->
            ColorButton(
                color = colorItem.color,
                isPremium = colorItem.isPremium,
                isSelected = colorItem.color == selectedColor,
                isUserPremium = isUserPremium
            ) { color ->
                if (!isUserPremium && colors.any { it.color == color && it.isPremium }) {
                    //If the user is NO premium, keep the previous value
                } else {
                    Log.d("MyErrors", ".color ${color}")
                    Log.d("MyErrors", ".toArgb() ${color.toArgb()}")
                    Log.d("MyErrors", ".convertColorToString() ${convertColorToString(color)}")
                    onColorChange(color)
                }
            }
        }
    }
}




@Composable
fun ColorButton(
    color: Color,
    isPremium: Boolean,
    isUserPremium: Boolean,
    isSelected: Boolean,
    onColorSelected: (Color) -> Unit
) {
    Box(
        modifier = Modifier.size(buttonSize + 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { onColorSelected(color) },
            colors = ButtonDefaults.buttonColors(
                containerColor = color
            ),
            modifier = Modifier
                .padding(3.dp)
                .border((0.5).dp, colorResource(id = R.color.secondary_color), CircleShape)
                .size(buttonSize)
        ){}

        if (isPremium && !isUserPremium) {
            Box(
                modifier = Modifier
                    .size(buttonSize)
                    .background(Color.Black.copy(0.3f), CircleShape)
            )
            Icon(
                Icons.Filled.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp),
            )
        }

        if (isSelected) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.White.copy(0.8f),
                modifier = Modifier.size(buttonSize)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, color, CircleShape)
            )
        }
    }
}