package com.galacticstudio.digidoro.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

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