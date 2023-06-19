package com.galacticstudio.digidoro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(

    //btns y asi
    primary = Gray60,
    onPrimary = White60,

    secondary = BabyGray30,
    onSecondary = White60,
    
    tertiary = AzureBlue10,
    onTertiary = Gray60,

    background = Gray60,
    onBackground = Gray60,

    //Surface colors affect surfaces of components, such as cards, sheets, and menus.
    surface = Gray60,
    onSurface = DarkNotes_accent,
    surfaceVariant = OnGray_accent, //menu's color hover
    onSurfaceVariant = DarkNotes_accent, //notes background



    //accents, ni modo, no hay otras variables
    //Gray from the note taking squares
    primaryContainer = LightGray_accent,
    secondaryContainer = Mint_accent,
    tertiaryContainer = BabyAzure_accent,
    onPrimaryContainer = HoneyYellow_accent,
    onTertiaryContainer = Lavender_accent,
    // for pomo bar
    error = Cherry_accent,
    onError = GreenPepper_accent,



    )

private val LightColorScheme = lightColorScheme(
    //https://m2.material.io/design/color/the-color-system.html#color-theme-creation

    primary = White60,
    onPrimary = Gray30,
    onPrimaryContainer = OnGray_accent, //menu's color hover

    secondary = Gray30,
    onSecondary = White60,

    tertiary = AzureBlue10,
    onTertiary = OnAzureBlue,

    background = White60,
    onBackground = White60,

    surface = White60,
    onSurface = Gray60,

    //accents, ni modo, no hay otras variables
    //Gray from the note taking squares
    primaryContainer = LightGray_accent,
    secondaryContainer = Mint_accent,
    tertiaryContainer = BabyAzure_accent,
    surfaceVariant = HoneyYellow_accent,
    onSurfaceVariant = Gray60,
    // for pomo bar
    error = Cherry_accent,
    onError = GreenPepper_accent,
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