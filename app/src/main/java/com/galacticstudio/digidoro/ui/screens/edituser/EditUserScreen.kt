package com.galacticstudio.digidoro.ui.screens.edituser

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
import com.galacticstudio.digidoro.ui.screens.edituser.viewmodel.EditUserViewModel
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.screens.noteitem.components.DottedDivider
import com.galacticstudio.digidoro.ui.screens.register.RegisterResponseState
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.DateInputTextField
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title

@Composable
fun EditUserScreen(
    navController: NavController,
    editUserViewModel: EditUserViewModel = viewModel(factory = EditUserViewModel.Factory),
) {
    // Retrieve the application instance from the current context
    val state = editUserViewModel.state // Retrieves the current state.value from the editUserViewModel.
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallGap = 11.dp
    val gap = 22.dp
    val largeGap = 40.dp

    LaunchedEffect(Unit) {
        editUserViewModel.onEvent(EditUserFormEvent.Rebuild)
    }

    LaunchedEffect(key1 = context) {
        // Collect the response events from the EditUserViewModel
        editUserViewModel.responseEvents.collect { event ->
            when (event) {
                is EditUserResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is EditUserResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is EditUserResponseState.Success -> {
                    Toast.makeText(
                        context,
                        "Your data has been updated.",
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
        Spacer(modifier = Modifier.height(largeGap))

        TextFieldForm(
            value = state.value.firstname,
            label = "Your firstname",
            placeholder = "John",
            type = TextFieldType.TEXT,
            isError = state.value.firstnameError != null,
        ) {
            editUserViewModel.onEvent(EditUserFormEvent.FirstnameChanged(it))
        }

        ErrorMessage(state.value.firstnameError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.value.lastname,
            label = "Your lastname",
            placeholder = "Doe",
            type = TextFieldType.TEXT,
            isError = state.value.lastnameError != null,
        ) {
            editUserViewModel.onEvent(EditUserFormEvent.LastnameChanged(it))
        }

        ErrorMessage(state.value.lastnameError)
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.value.username,
            label = "Your username",
            placeholder = "username",
            type = TextFieldType.TEXT,
            isError = state.value.usernameError != null,
        ) {
            editUserViewModel.onEvent(EditUserFormEvent.UsernameChanged(it))
        }
        ErrorMessage(state.value.usernameError)

        Spacer(modifier = Modifier.height(35.dp))
        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        TextFieldForm(
            value = state.value.phoneNumber,
            label = "Your phone number",
            placeholder = "77777777",
            type = TextFieldType.NUMBER,
            isError = state.value.phoneNumberError != null,
        ) {
            editUserViewModel.onEvent(EditUserFormEvent.PhoneNumberChanged(it))
        }

        ErrorMessage(state.value.phoneNumberError)
        Spacer(modifier = Modifier.height(35.dp))

        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        DateInputTextField(
            value = state.value.birthday,
            error = state.value.birthdayError != null
        ) {
            editUserViewModel.onEvent(EditUserFormEvent.BirthdayChanged(it))
        }

        ErrorMessage(state.value.birthdayError)
        Spacer(modifier = Modifier.height(35.dp))

        DottedDivider()
        Spacer(modifier = Modifier.height(gap))

        CustomButton(
            text = "Update User",
            onClick = {
                editUserViewModel.onEvent(EditUserFormEvent.Submit)
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
fun RegisterHeader() {
    val message = CustomMessageData(
        title = "Update user",
        subTitle = "Modify user information and unleash the power."
    )

    Title(
        message = message,
        alignment = Alignment.Start
    )
}