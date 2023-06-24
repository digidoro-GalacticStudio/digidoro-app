package com.galacticstudio.digidoro.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.screens.account.components.options.OptionComposable
import com.galacticstudio.digidoro.ui.screens.account.components.options.OptionsComposable
import com.galacticstudio.digidoro.ui.screens.account.components.userInformation.UserInformation
import com.galacticstudio.digidoro.ui.shared.button.VerticalDivider
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme


@Composable
@Preview(showSystemUi = true)
fun AccountPreview(){
    DigidoroTheme() {
        AccountScreen(navController = rememberNavController())

    }
}

@Composable
fun AccountScreen(
    navController: NavHostController = rememberNavController()
){
    AccountContent()
}

@Composable
fun AccountContent(){

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            UserInformation(
                userName = "Jenny",
                profilePic = R.drawable.arrow_back,
                levelName = "dreamer",
                currentScore = 85,
                nextLevelScore = 250
            )

            Spacer(modifier = Modifier.height(30.dp))
            OptionsComposable()

        }

        OptionComposable(
            title = "Cerrar sesión",
            icon = R.drawable.arrow_back,
            description = "Logout",
            backgroundColor = MaterialTheme.colorScheme.secondary,
            color = MaterialTheme.colorScheme.primary
            ) {
            
        }
    }
}