package com.galacticstudio.digidoro.ui.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.Nunito

@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordPreview() {
    val navController = rememberNavController()
    ForgotPasswordScreen(navController = navController)
}

@Composable
fun ForgotPasswordScreen(
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
        Header()
        Spacer(modifier = Modifier.height(largegap))
        TextFieldForm(
            value = "",
            label = "Your username",
            placeholder = "digidoro4U",
            type = TextFieldType.TEXT,
            leadingIcon = painterResource(R.drawable.email_icon),
            onTextFieldChanged = { /* TODO */}
        )
        Spacer(modifier = Modifier.height(largegap))
        VerificationInstructions()
        Spacer(modifier = Modifier.height(largegap))
        CustomButton(
            text = "Next",
            onClick = { navController.navigate(route = Screen.VerifyAccount.route) }
        )
        Spacer(modifier = Modifier.height(gap))
        RegisterButton(
            text = "return",
            onClick = { navController.popBackStack() }
        )
    }
}

@Composable
fun Header() {
    val message = CustomMessageData(
        title = "Account Recovery",
        subTitle = "Please enter your email address to begin the password reset process."
    )

    Title(
        message = message,
        alignment = Alignment.Start
    )
}

@Composable
fun VerificationInstructions() {
    Text(
        text = "We will send an authentication code to your email. Please save it, as you will need it later to verify your identity.",
        style =  MaterialTheme.typography.bodySmall,
        fontFamily = Nunito,
        color = Color(0xFF202124)
    )
}