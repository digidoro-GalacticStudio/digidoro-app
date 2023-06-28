package com.galacticstudio.digidoro.ui.screens.account.components.options

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

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
    val navController = rememberNavController()
    DigidoroTheme() {
        OptionsComposable(navController)
    }
}

@Composable
fun OptionsComposable(
    navController: NavController,
){
    val context = LocalContext.current

    Column(modifier =  Modifier.verticalScroll(rememberScrollState())) {
        OptionComposable(
            title = "Change user Settings",
            icon = R.drawable.settings_icon,
            description = "Settings icon"
        ){
            navController.navigate(Screen.EditUser.route)
        }
        OptionComposable(
            title = "Your Achievements",
            icon = R.drawable.medal_icon,
            description = "Achievements icon"
        ){

        }
        OptionComposable(
            title = "Check Your Ranking",
            icon = R.drawable.partner_exchange_icon,
            description = "Ranking icon"
        ){
            navController.navigate(Screen.Ranking.route)
        }
        OptionComposable(
            title = "Create Your Pomodoros",
            icon = R.drawable.pomo,
            description = "Pomodoro icon"

        ){
            navController.navigate(Screen.Pomodoro.route)
        }
        OptionComposable(
            title = "Upgrade to Pro+",
            icon = R.drawable.fire_icon,
            description = "Enable Pro icon"
        ){

        }
        OptionComposable(
            title = "Rate Us on Play Store",
            icon = R.drawable.level_star_icon,
            description = "Rate on Play Store icon"
        ){
            //TODO REPLACE THIS
            val appPackageName = "com.example.myapp"
            val playStoreUrl = "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
            val intent = Intent(Intent.ACTION_VIEW, playStoreUrl)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        }
    }
}