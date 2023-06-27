package com.galacticstudio.digidoro.ui.screens.forgotpassword

import android.util.Log
import android.widget.Toast
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
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.forgotpassword.viewmodel.ForgotPasswordViewModel
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
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
    navController: NavController,
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(factory = ForgotPasswordViewModel.Factory),
) {
    val state =
        forgotPasswordViewModel.state // Retrieves the current state from the forgotPasswordViewModel.

    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallGap = 11.dp
    val gap = 22.dp
    val largeGap = 40.dp

    LaunchedEffect(key1 = context) {
        // Collect the response events from the forgotPasswordViewModel
        forgotPasswordViewModel.responseEvents.collect { event ->
            when (event) {
                is ForgotPasswordResponse.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ForgotPasswordResponse.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ForgotPasswordResponse.Success -> {
                    navController.navigate(Screen.VerifyAccount.route + "?email=${state.value.email}")
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
        Spacer(modifier = Modifier.height(largeGap))

        TextFieldForm(
            value = state.value.email,
            label = "Your email",
            placeholder = "digidoro4U@gmail.com",
            type = TextFieldType.TEXT,
            leadingIcon = painterResource(R.drawable.email_icon),
            isError = state.value.emailError != null,
        ) {
            forgotPasswordViewModel.onEvent(ForgotPasswordFormEvent.EmailChanged(it))
        }

        ErrorMessage(state.value.emailError)
        Spacer(modifier = Modifier.height(largeGap))

        VerificationInstructions()
        Spacer(modifier = Modifier.height(largeGap))

        CustomButton(
            text = "Next",
            onClick = {
                forgotPasswordViewModel.onEvent(ForgotPasswordFormEvent.Submit)
            }
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
        style = MaterialTheme.typography.bodyMedium,
        fontFamily = Nunito,
    )
}