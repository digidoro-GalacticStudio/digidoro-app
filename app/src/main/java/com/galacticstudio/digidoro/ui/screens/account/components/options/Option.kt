package com.galacticstudio.digidoro.ui.screens.account.components.options

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.ui.theme.White60

@Composable
fun OptionComposable(
    title: String,
    icon: Int,
    description: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    color: Color = MaterialTheme.colorScheme.secondary,
    onOptionClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOptionClick()
            }
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 13.dp)
    ){
        Row() {
            Image(
                painter = painterResource(id = icon),
                contentDescription = description,
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(color),
                alignment = Alignment.Center)

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OptionsPreview(){
    DigidoroTheme() {
        OptionsComposable()
    }
}

@Composable
fun OptionsComposable(){
    Column(modifier =  Modifier.verticalScroll(rememberScrollState())) {
        OptionComposable(
            title = "Configuración general",
            icon = R.drawable.settings_icon,
            description = "Icono de configuración"
        ){}
        OptionComposable(
            title = "Tus logros",
            icon = R.drawable.medal_icon,
            description = "Icono de logros"
        ){}
        OptionComposable(
            title = "Verifica tu ranking",
            icon = R.drawable.partner_exchange_icon,
            description = "Icono de ranking"

        ){}
        OptionComposable(
            title = "Configura tus pomodoros",
            icon = R.drawable.pomo,
            description = "Icono de pomodoro"

        ){}
        OptionComposable(
            title = "Pasate a Pro+",
            icon = R.drawable.fire_icon,
            description = "Icono de habilitar pro"
        ){}
        OptionComposable(
            title = "Califícanos en la Play Store",
            icon = R.drawable.level_star_icon,
            description = "Icono de calificar en play store"

        ){}
    }
}