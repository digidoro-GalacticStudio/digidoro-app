package com.galacticstudio.digidoro.ui.forgotpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.screens.login.Logo
import com.galacticstudio.digidoro.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.AzureBlue10
import com.galacticstudio.digidoro.ui.theme.Nunito

@Preview(showSystemUi = true)
@Composable
fun VerifyAccountPreview() {
    val navController = rememberNavController()
    VerifyAccountScreen(navController = navController)

}

@Composable
fun VerifyAccountScreen(
    navController: NavController
) {
    val smallgap = 11.dp
    val gap = 22.dp
    val largegap = 40.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        Logo()
        Spacer(modifier = Modifier.height(gap))
        HeaderVerify()
        Spacer(modifier = Modifier.height(smallgap))
        EmailHint("digidoro4u@hotmail.com")
        Spacer(modifier = Modifier.height(largegap))
        Text(
            text = "We have sent an authentication code to your email. Please enter it to verify that it's you.",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = Nunito,
            color = Color(0xFF202124)
        )
        Spacer(modifier = Modifier.height(smallgap))
        TextFieldForm(
            label = "Code",
            placeholder = "Verification code",
            type = TextFieldType.NUMBER,
            leadingIcon = painterResource(R.drawable.tag_icon),
        )
        Spacer(modifier = Modifier.height(largegap))
        Text(
            text = "Haven't received the message?",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = Nunito,
            color = Color(0xFF202124)
        )
        Spacer(modifier = Modifier.height(smallgap))
        ResendCode(
            text = "Resend it.",
            onClick = { /* Action */ }
        )
        Spacer(modifier = Modifier.height(largegap))
        CustomButton(
            text = "Next",
            onClick = { navController.navigate(HOME_GRAPH_ROUTE) }
        )
        Spacer(modifier = Modifier.height(gap))
        RegisterButton(
            text = "return",
            onClick = { navController.popBackStack() }
        )
    }
}

@Composable
fun HeaderVerify() {
    val message = CustomMessageData(
        title = "Verify Account",
        subTitle = ""
    )

    Title(
        message = message,
        alignment = Alignment.CenterHorizontally
    )
}

@Composable
fun ResendCode(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .clickable { onClick() },
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.W800,
        color = AzureBlue10
    )
}

@Composable
fun EmailHint(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFF7A7A7A),
            ),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.email_icon),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "digidoro4u@hotmail.com",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Nunito,
                    color = Color(0xFF202124)
                )
            }
        }
    }

}