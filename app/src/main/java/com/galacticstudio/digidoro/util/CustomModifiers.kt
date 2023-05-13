package com.galacticstudio.digidoro.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Adds a shadow to a composable, with a rounded corner. The shadow's position and color can be
 * customized with the optional parameters.
 *
 * @param cornerRadius The corner radius of the rounded rectangle, in density-independent pixels.
 * @param shadowOffset The offset of the shadow from the composable's top-left corner.
 * @param shadowColor The color of the shadow.
 * @return A Modifier with the shadow and corner applied.
 */

fun Modifier.shadowWithCorner(
    cornerRadius: Dp,
    shadowOffset: Offset = Offset.Zero,
    shadowColor: Color = Color.Black,
): Modifier {
    return drawBehind {
        drawRoundRect(
            color = shadowColor,
            topLeft = shadowOffset,
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx())
        )
    }.clip(RoundedCornerShape(cornerRadius))
}

/**
 * Modifier that adds a shadow and a border with rounded corners to a composable.
 *
 * @param cornerRadius The corner radius of the rounded corners.
 * @param borderWidth The width of the border.
 * @param borderColor The color of the border.
 * @param shadowOffset The offset of the shadow from the composable.
 * @param shadowColor The color of the shadow.
 *
 * @return A Modifier with the shadow and border with rounded corners.
 */

fun Modifier.shadowWithBorder(
    cornerRadius: Dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Black,
    shadowOffset: Offset = Offset.Zero,
    shadowColor: Color = Color.Black,
): Modifier {
    return drawBehind {
        drawRoundRect(
            color = borderColor,
            topLeft = shadowOffset,
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = borderWidth.toPx())
        )
        drawRoundRect(
            color = shadowColor,
            topLeft = shadowOffset,
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
        )
    }.clip(RoundedCornerShape(cornerRadius))
}