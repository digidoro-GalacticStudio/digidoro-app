package com.galacticstudio.digidoro.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.galacticstudio.digidoro.ui.theme.Gray60

class ColorCustomUtils {
    companion object {
        fun addColorTone(color: Color, tone: Color): Color {
            return color.run {
                Color(
                    red = (this.red * tone.red).coerceIn(0f, 1f),
                    green = (this.green * tone.green).coerceIn(0f, 1f),
                    blue = (this.blue * tone.blue).coerceIn(0f, 1f),
                    alpha = this.alpha
                )
            }
        }

        fun returnLuminanceColor(color: Color) : Color {
            return if (isColorDark(color)) Color.White else Gray60
        }

        fun isColorDark(color: Color): Boolean {
            val luminance = ColorUtils.calculateLuminance(color.toArgb())
            return luminance < 0.5
        }
    }
}