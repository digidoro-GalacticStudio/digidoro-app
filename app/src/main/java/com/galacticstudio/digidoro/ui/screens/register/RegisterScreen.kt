package com.galacticstudio.digidoro.ui.screens.register

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.HOME_GRAPH_ROUTE
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.screens.noteitem.components.DottedDivider
import com.galacticstudio.digidoro.ui.screens.register.viewmodel.RegisterViewModel
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.DateInputTextField
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

/**
 * Composable function for displaying the preview of the Register screen.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RegisterScreen(navController = navController)
        }
    }
}

/**
 * Composable function for displaying the Register screen.
 *
 * @param navController The NavController used for navigation.
 * @param registerViewModel The RegisterViewModel used for managing the Register logic.
 */
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
) {
    // Retrieve the application instance from the current context
    //    val app: RetrofitApplication = LocalContext.current.applicationContext as RetrofitApplication
    val state = registerViewModel.state // Retrieves the current state from the RegisterViewModel.
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallgap = 11.dp
    val gap = 22.dp
    val largegap = 40.dp

    /*
     * Executes a side effect using LaunchedEffect when the context value changes.
     * This effect collects response events from the RegisterViewModel and performs different actions based on the event type.
     * -> If the event is of type Error or ErrorWithMessage, displays a toast with the exception message.
     * -> If the event is of type Success, displays a toast with the Register success message and saves the auth token.
     * This effect relies on the provided context to access resources like Toast and the RetrofitApplication instance.
     */
    LaunchedEffect(key1 = context) {
        // Collect the response events from the RegisterViewModel
        registerViewModel.responseEvents.collect { event ->
            when (event) {
                is RegisterResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is RegisterResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is RegisterResponseState.Success -> {
                    navController.navigate(Screen.Login.route)

                    Toast.makeText(
                        context,
                        "Register Successful ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(35.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Logo()
        Spacer(modifier = Modifier.height(gap))

        RegisterHeader()
        Spacer(modifier = Modifier.height(largegap))

        TextFieldForm(
            value = state.firstname,
            label = "Your firstname",
            placeholder = "John",
            type = TextFieldType.TEXT,
            isError = state.firstnameError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.FirstnameChanged(it))
        }

        ErrorMessage(state.firstnameError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.lastname,
            label = "Your lastname",
            placeholder = "Doe",
            type = TextFieldType.TEXT,
            isError = state.lastnameError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.LastnameChanged(it))
        }

        ErrorMessage(state.lastnameError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.username,
            label = "Your username",
            placeholder = "username",
            type = TextFieldType.TEXT,
            isError = state.usernameError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.UsernameChanged(it))
        }
        ErrorMessage(state.usernameError)

        Spacer(modifier = Modifier.height(35.dp))
        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.email,
            label = "Your email",
            placeholder = "digidoro4U@hotmail.com",
            type = TextFieldType.TEXT,
            leadingIcon = painterResource(R.drawable.email_icon),
            isError = state.emailError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.EmailChanged(it))
        }

        ErrorMessage(state.emailError)
        Spacer(modifier = Modifier.height(35.dp))

        TextFieldForm(
            value = state.phoneNumber,
            label = "Your phone number",
            placeholder = "77777777",
            type = TextFieldType.NUMBER,
            isError = state.phoneNumberError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.PhoneNumberChanged(it))
        }

        ErrorMessage(state.phoneNumberError)
        Spacer(modifier = Modifier.height(35.dp))

        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        DateInputTextField(
            value = state.birthday,
            error = state.birthdayError != null
        ) {
            registerViewModel.onEvent(RegisterFormEvent.BirthdayChanged(it))
        }

        ErrorMessage(state.birthdayError)
        Spacer(modifier = Modifier.height(35.dp))

        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.password,
            label = "Your password",
            placeholder = "e.g: *******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.passwordError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.PasswordChanged(it))
        }

        ErrorMessage(state.passwordError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.repeatedPassword,
            label = "Confirm your password",
            placeholder = "e.g: *******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.repeatedPasswordError != null,
        ) {
            registerViewModel.onEvent(RegisterFormEvent.ConfirmPasswordChanged(it))
        }

        ErrorMessage(state.repeatedPasswordError)
        Spacer(modifier = Modifier.height(largegap))

        CustomButton(
            text = "Register",
            onClick = {
                registerViewModel.onEvent(RegisterFormEvent.Submit)
            }
        )
        Spacer(modifier = Modifier.height(gap))
        RegisterButton(
            text = "log in",
            onClick = { navController.navigate(route = Screen.Login.route) }
        )
    }
}

@Composable
fun RegisterHeader() {
    val message = CustomMessageData(
        title = "Sign up",
        subTitle = "Create an account and feel the magic."
    )

    Title(
        message = message,
        alignment = Alignment.Start
    )
}