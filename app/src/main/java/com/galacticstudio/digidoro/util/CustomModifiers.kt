package com.galacticstudio.digidoro.util

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
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
    }
}

fun Modifier.dropShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

fun Modifier.shimmerEffect(borderRadius: Dp = 0.dp): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = RoundedCornerShape(borderRadius)
    )
        .onGloballyPositioned {
            size = it.size
        }
}
