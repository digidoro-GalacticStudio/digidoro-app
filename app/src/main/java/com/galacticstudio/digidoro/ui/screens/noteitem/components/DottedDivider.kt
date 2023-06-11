package com.galacticstudio.digidoro.ui.screens.noteitem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * A composable function representing a dotted divider.
 *
 * @param color The color of the divider.
 */
@Composable
fun DottedDivider(
    color: Color = Color.Black
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Canvas(Modifier.fillMaxWidth()) {
        drawRoundRect(color = color, style = stroke)
    }
}