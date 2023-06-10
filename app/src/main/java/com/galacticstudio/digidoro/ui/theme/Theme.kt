package com.galacticstudio.digidoro.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Gray60,
    secondary = BabyGray30,
    tertiary = AzureBlue10
)

private val LightColorScheme = lightColorScheme(
    primary = White60,
    secondary = Gray30,
    tertiary = AzureBlue10
)


@Composable
fun DigidoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable ()-> Unit
    ){
    MaterialTheme(
        typography = Typography,
        colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme ,
        content = content
    )
}