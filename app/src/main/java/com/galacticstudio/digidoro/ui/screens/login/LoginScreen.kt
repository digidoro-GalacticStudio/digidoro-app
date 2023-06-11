package com.galacticstudio.digidoro.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title


@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}

@Composable
fun LoginScreen(
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
            label = "Your username",
            placeholder = "digidoro4U@hotmail.com",
            type = TextFieldType.TEXT,
            leadingIcon = painterResource(R.drawable.email_icon),
        )
        Spacer(modifier = Modifier.height(gap))
        TextFieldForm(
            label = "Your password",
            placeholder = "*******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(smallgap))
        ForgotPassword(
            text = "forgot your password?",
            onClick = { navController.navigate(route = Screen.ForgotPassword.route) }
        )
        Spacer(modifier = Modifier.height(gap))
        CustomButton(
            text = "Log in",
            onClick = { navController.navigate(HOME_GRAPH_ROUTE) }
        )
        Spacer(modifier = Modifier.height(gap))
        RegisterButton(
            text = "register",
            onClick = { navController.navigate(route = Screen.SignUp.route) }
        )
    }
}

@Composable
fun Logo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.digi_login_light_mode),
            contentDescription = null,
            modifier = Modifier.width(175.dp)
        )
    }
}

@Composable
fun Header() {
    val message = CustomMessageData(
        title = "Log in",
        subTitle = "Sign in to continue"
    )

    Title(
        message = message,
        alignment = Alignment.Start
    )
}

@Composable
fun ForgotPassword(text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = text,
            modifier = Modifier
                .clickable { onClick() },
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF7A7A7A)
        )
    }
}

@Composable
fun RegisterButton(text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            modifier = Modifier
                .clickable { onClick() },
            textDecoration = TextDecoration.Underline,
            fontSize = 20.sp,
            color = Color(0xFF202124)
        )
    }
}
