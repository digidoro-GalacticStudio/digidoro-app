package com.galacticstudio.digidoro.ui.screens.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.network.retrofit.RetrofitInstance
import com.galacticstudio.digidoro.ui.screens.login.viewmodel.LoginViewModel
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

/**
 * Composable function for displaying the preview of the login screen.
 */
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(navController = navController)
        }
    }
}

/**
 * Composable function for displaying the login screen.
 *
 * @param navController The NavController used for navigation.
 * @param loginViewModel The LoginViewModel used for managing the login logic.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
) {
    // Retrieve the application instance from the current context
    val app: RetrofitApplication = LocalContext.current.applicationContext as RetrofitApplication

    val state = loginViewModel.state // Retrieves the current state from the loginViewModel.
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallgap = 11.dp
    val gap = 22.dp
    val largegap = 40.dp

    /*
     * Executes a side effect using LaunchedEffect when the context value changes.
     * This effect collects response events from the loginViewModel and performs different actions based on the event type.
     * -> If the event is of type Error or ErrorWithMessage, displays a toast with the exception message.
     * -> If the event is of type Success, displays a toast with the login success message and saves the auth token.
     * This effect relies on the provided context to access resources like Toast and the RetrofitApplication instance.
     */
    LaunchedEffect(key1 = context){
        // Collect the response events from the loginViewModel
        loginViewModel.responseEvents.collect { event ->
            when (event) {
                is LoginResponseState.Error -> {
                    Toast.makeText(context, "An error has occurred ${event.exception}", Toast.LENGTH_SHORT).show()
                }

                is LoginResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is LoginResponseState.Success -> {
                    app.saveAuthToken(event.token)
                    app.saveRoles(event.roles)
                    app.saveUsername(event.username)
                    val tokenValue = app.getToken()

                    Toast.makeText(
                        context,
                        "Login Successful ${tokenValue}",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(HOME_GRAPH_ROUTE)
                }

                else -> {}
            }
        }
    }

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
            value = state.email,
            isError = state.emailError != null,
        ) {
            loginViewModel.onEvent(LoginFormEvent.EmailChanged(it))
        }

        ErrorMessage(state.emailError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.password,
            label = "Your password",
            placeholder = "*******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.passwordError != null,
        ){
            loginViewModel.onEvent(LoginFormEvent.PasswordChanged(it))
        }

        ErrorMessage(state.passwordError)
        Spacer(modifier = Modifier.height(smallgap))

        ForgotPassword(
            text = "forgot your password?",
            onClick = { navController.navigate(route = Screen.ForgotPassword.route) }
        )
        Spacer(modifier = Modifier.height(gap))

        CustomButton(
            text = "Log in",
            onClick = {
                loginViewModel.onEvent(LoginFormEvent.Submit)
            }
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
    val imageRes = if (isSystemInDarkTheme()) {
        R.drawable.digi_login_dark_mode
    } else {
        R.drawable.digi_login_light_mode
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Digidoro App",
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
            color = MaterialTheme.colorScheme.onPrimary.copy(0.4f)
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