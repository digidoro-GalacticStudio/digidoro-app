package com.galacticstudio.digidoro.ui.screens.editcredentials

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.editcredentials.components.EditCredentialsViewModel
import com.galacticstudio.digidoro.ui.screens.forgotpassword.ForgotPasswordResponse
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.screens.register.viewmodel.RegisterViewModel
import com.galacticstudio.digidoro.ui.screens.verifyaccount.EmailHint
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title

@Composable
fun EditCredentialsScreen(
    email: String,
    recoveryCode: Int,
    navController: NavController,
    editCredentialsViewModel: EditCredentialsViewModel = viewModel(factory = EditCredentialsViewModel.Factory),
) {
    val state = editCredentialsViewModel.state // Retrieves the current state from the editCredentialsViewModel.
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallGap = 11.dp
    val gap = 22.dp
    val largeGap = 40.dp

    LaunchedEffect(Unit) {
        editCredentialsViewModel.onEvent(EditCredentialsFormEvent.VerificationCodeChanged(recoveryCode))
        editCredentialsViewModel.onEvent(EditCredentialsFormEvent.EmailChanged(email))
    }

    LaunchedEffect(key1 = context) {
        editCredentialsViewModel.responseEvents.collect { event ->
            when (event) {
                is EditCredentialsResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is EditCredentialsResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is EditCredentialsResponseState.Success -> {
                    navController.navigate(route = Screen.Login.route)
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

        EmailHint(state.value.email)
        Spacer(modifier = Modifier.height(largeGap))

        TextFieldForm(
            value = state.value.password,
            label = "Your new password",
            placeholder = "e.g: *******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.value.passwordError != null,
        ) {
            editCredentialsViewModel.onEvent(EditCredentialsFormEvent.PasswordChanged(it))
        }

        ErrorMessage(state.value.passwordError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.value.repeatedPassword,
            label = "Confirm your new password",
            placeholder = "e.g: *******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.value.repeatedPasswordError != null,
        ) {
            editCredentialsViewModel.onEvent(EditCredentialsFormEvent.ConfirmPasswordChanged(it))
        }

        ErrorMessage(state.value.repeatedPasswordError)
        Spacer(modifier = Modifier.height(largeGap))

        CustomButton(
            text = "Update password",
            onClick = { editCredentialsViewModel.onEvent(EditCredentialsFormEvent.Submit) }
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
        title = "Change password",
        subTitle = "Create a strong password."
    )

    Title(
        message = message,
        alignment = Alignment.Start
    )
}
