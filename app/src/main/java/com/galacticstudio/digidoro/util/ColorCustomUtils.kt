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

        fun convertColorToString(color: Color): String {
            val red = (color.red * 255).toInt()
            val green = (color.green * 255).toInt()
            val blue = (color.blue * 255).toInt()
            return String.format("%02X%02X%02X", red, green, blue)
        }

        fun returnLuminanceColor(color: Color): Color {
            return if (isColorDark(color)) Color.White else Gray60
        }

        fun isColorDark(color: Color): Boolean {
            val luminance = ColorUtils.calculateLuminance(color.toArgb())
            return luminance < 0.5
        }

        fun adjustColorBrightness(color: Color, isDarken: Boolean, factor: Float): Color {
            val argb = color.toArgb()

            val red = android.graphics.Color.red(argb)
            val green = android.graphics.Color.green(argb)
            val blue = android.graphics.Color.blue(argb)
            val alpha = android.graphics.Color.alpha(argb)

            val hsv = FloatArray(3)
            android.graphics.Color.RGBToHSV(red, green, blue, hsv)

            if (isDarken) {
                hsv[2] *= (1 - factor)
            } else {
                hsv[2] += (1 - hsv[2]) * factor
            }

            val adjustedArgb = android.graphics.Color.HSVToColor(hsv)
            return Color(
                android.graphics.Color.argb(
                    alpha,
                    android.graphics.Color.red(adjustedArgb),
                    android.graphics.Color.green(adjustedArgb),
                    android.graphics.Color.blue(adjustedArgb)
                )
            )
        }

    }
}