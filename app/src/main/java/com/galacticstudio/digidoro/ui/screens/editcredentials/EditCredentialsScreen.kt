package com.galacticstudio.digidoro.ui.screens.editcredentials

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.galacticstudio.digidoro.ui.screens.editcredentials.components.EditCredentialsViewModel
import com.galacticstudio.digidoro.ui.screens.login.Logo
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
    editCredentialsViewModel: EditCredentialsViewModel = viewModel(factory = RegisterViewModel.Factory),
) {
    val state = editCredentialsViewModel.state.value // Retrieves the current state from the loginViewModel.

    val smallGap = 11.dp
    val gap = 22.dp
    val largeGap = 40.dp

    LaunchedEffect(Unit) {
        editCredentialsViewModel.onEvent(EditCredentialsFormEvent.VerificationCodeChanged(recoveryCode))
        editCredentialsViewModel.onEvent(EditCredentialsFormEvent.EmailChanged(email))
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
        Spacer(modifier = Modifier.height(smallGap))

        EmailHint("digidoro4u@hotmail.com")
        Spacer(modifier = Modifier.height(largeGap))

        TextFieldForm(
            value = state.password,
            label = "Your password",
            placeholder = "*******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.passwordError != null,
        ) {
            editCredentialsViewModel.onEvent(EditCredentialsFormEvent.PasswordChanged(it))
        }

        ErrorMessage(state.passwordError)
        Spacer(modifier = Modifier.height(smallGap))

        TextFieldForm(
            value = state.password,
            label = "Your password",
            placeholder = "*******",
            type = TextFieldType.PASSWORD,
            leadingIcon = null,
            isPassword = true,
            isError = state.repeatedPasswordError != null,
        ) {
            editCredentialsViewModel.onEvent(EditCredentialsFormEvent.ConfirmPasswordChanged(it))
        }

        ErrorMessage(state.repeatedPassword)
        Spacer(modifier = Modifier.height(smallGap))

        CustomButton(
            text = "Update",
            onClick = { editCredentialsViewModel.onEvent(EditCredentialsFormEvent.Submit) }
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
