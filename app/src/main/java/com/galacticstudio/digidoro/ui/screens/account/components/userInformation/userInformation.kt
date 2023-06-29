package com.galacticstudio.digidoro.ui.screens.account.components.userInformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.AzureBlue10
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.ui.theme.Nunito


@Preview(showSystemUi = true)
@Composable
fun InformationPreview() {
    DigidoroTheme {
        UserInformation(
            userName = "Jenny",
            R.drawable.manage_account_icon,
            levelName = "dreamer",
            currentScore = 85,
            nextLevelScore = 250
        )
    }
}

@Composable
fun UserInformation(
    userName: String = "User_1234",
    profilePic: Int,
    levelName: String,
    currentScore: Int,
    nextLevelScore: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Title()
        Spacer(modifier = Modifier.height(16.dp))
        UserData(userName = userName, imgProfile = profilePic)
        Spacer(modifier = Modifier.height(16.dp))
        AccountData(
            levelName = levelName,
            currentScore = currentScore,
            nextLevelScore = nextLevelScore
        )
    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        Row() {
            Text(
                text = "Your",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = " digidoro ",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AzureBlue10
            )
            Text(
                text = "in your hands",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun UserData(
    imgProfile: Int,
    userName: String = "User_14657"
) {
    Box(
        modifier = Modifier
            .clickable { }
    ) {

        Image(
            painter = painterResource(id = imgProfile),
            contentDescription = "User's profile pic",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(150f)
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(size = 150f)
                )
                .padding(4.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.settings_icon),
            contentDescription = "Change profile pic icon",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .offset(
                    x = 65.dp,
                    y = 55.dp
                )
                .size(34.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(100f)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(100f)
                )
                .padding(4.dp)
        )

    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = userName,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun AccountData(
    levelName: String,
    currentScore: Int,
    nextLevelScore: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Nivel")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = levelName,
                fontSize = 14.sp,
                fontFamily = Nunito,
            )
            Text(
                text = " ${currentScore}/${nextLevelScore} EXP",
                style = TextStyle(color = colorResource(id = R.color.accent_color)),
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                fontFamily = Nunito,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = currentScore.toFloat() / nextLevelScore.toFloat(),
            modifier = Modifier
                .width(140.dp)
                .height(5.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(5.dp)
                ),
            color = Color(0xFF4981FF),
            trackColor = MaterialTheme.colorScheme.primary,

            )
    }
}