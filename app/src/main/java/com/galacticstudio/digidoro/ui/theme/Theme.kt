package com.galacticstudio.digidoro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    //btns
    primary = Gray60,
    onPrimary = White60,

    secondary = BabyGray30,
    onSecondary = White60,

    tertiary = AzureBlue10,
    onTertiary = DarkNotes_accent,

    background = Gray60,
    onBackground = Gray60,

    //Surface colors affect surfaces of components, such as cards, sheets, and menus.
    surface = Gray60,
    onSurface = DarkNotes_accent,
    surfaceVariant = OnGray_accent, //menu's color hover
    onSurfaceVariant = DarkNotes_accent, //notes background

    //Gray from the note taking squares
    primaryContainer = DarkNotes_accent,
    onPrimaryContainer = White60,
    secondaryContainer = OnGray_accent,
    onSecondaryContainer = DarkNotes_accent,
    tertiaryContainer = DarkNotes_accent,
    onTertiaryContainer = Lavender_accent,

    error = Cherry_accent,
    onError = GreenPepper_accent,
)

private val LightColorScheme = lightColorScheme(
    //https://m2.material.io/design/color/the-color-system.html#color-theme-creation
    primary = White60,
    onPrimary = Gray30,

    secondary = Gray30,
    onSecondary = White60,

    tertiary = AzureBlue10,
    onTertiary = OnAzureBlue,

    background = White60,
    onBackground = White60,

    surface = White60,
    onSurface = Gray60,

    //Gray from the note taking squares
    primaryContainer = White60,
    onPrimaryContainer = OnGray_accent, //menu's color hover
    secondaryContainer = OnGray_accent,
    onSecondaryContainer = BabyGray30,
    tertiaryContainer = LightGray_accent,

    surfaceVariant = HoneyYellow_accent,
    onSurfaceVariant = Gray60,

    error = Cherry_accent,
    onError = GreenPepper_accent,
)


@Composable
fun DigidoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}