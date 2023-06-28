package com.galacticstudio.digidoro.ui.screens.verifyaccount

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.screens.verifyaccount.viewmodel.VerifyAccountViewModel
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.textfield.ErrorMessage
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldForm
import com.galacticstudio.digidoro.ui.shared.textfield.TextFieldType
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.AzureBlue10
import com.galacticstudio.digidoro.ui.theme.Nunito
import kotlinx.coroutines.delay

@Preview(showSystemUi = true)
@Composable
fun VerifyAccountPreview() {
//    val navController = rememberNavController()
//    VerifyAccountScreen(navController = navController)
}

@Composable
fun VerifyAccountScreen(
    email: String,
    navController: NavController,
    verifyAccountViewModel: VerifyAccountViewModel = viewModel(factory = VerifyAccountViewModel.Factory),
) {
    val state =
        verifyAccountViewModel.state // Retrieves the current state from the verifyAccountViewModel.

    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    val smallGap = 11.dp
    val gap = 22.dp
    val largeGap = 40.dp

    LaunchedEffect(Unit) {
        verifyAccountViewModel.onEvent(VerifyAccountFormEvent.EmailChanged(email))
    }

    var countdownSeconds by remember { mutableStateOf(30) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = countdownSeconds) {
        if (countdownSeconds > 0 && !isButtonEnabled) {
            delay(1000)
            countdownSeconds--
        } else {
            isButtonEnabled = true
        }
    }

    LaunchedEffect(key1 = context) {
        // Collect the response events from the verifyAccountViewModel
        verifyAccountViewModel.responseEvents.collect { event ->
            when (event) {
                is VerifyAccountResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is VerifyAccountResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is VerifyAccountResponseState.Success -> {
                    if (event.complete) {
                        navController.navigate(Screen.EditCredentials.route + "?email=${email}&recoveryCode=${state.value.code}")
                    } else {
                        Toast.makeText(
                            context,
                            "Code resent successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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

        HeaderVerify()
        Spacer(modifier = Modifier.height(smallGap))

        EmailHint(email)
        Spacer(modifier = Modifier.height(largeGap))

        Text(
            text = "We have sent an authentication code to your email. Please enter it to verify that it's you.",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = Nunito,
        )
        Spacer(modifier = Modifier.height(smallGap))

        TextFieldForm(
            value = state.value.code,
            label = "Code",
            placeholder = "Verification code",
            type = TextFieldType.NUMBER,
            leadingIcon = painterResource(R.drawable.tag_icon),
            isError = state.value.codeError != null,
        ) {
            verifyAccountViewModel.onEvent(VerifyAccountFormEvent.CodeChanged(it))
        }

        ErrorMessage(state.value.codeError)
        Spacer(modifier = Modifier.height(largeGap))

        Text(
            text = "Haven't received the message?",
            style =  MaterialTheme.typography.bodyMedium,
            fontFamily = Nunito,
        )
        Spacer(modifier = Modifier.height(smallGap))

        Column {
            ResendCode(
                isButtonEnabled = isButtonEnabled,
                text = "Resend it.",
                onClick = {
                    verifyAccountViewModel.onEvent(VerifyAccountFormEvent.ResendCode)
                    countdownSeconds = 30
                    isButtonEnabled = false
                }
            )

            if (!isButtonEnabled) {
                val countdownText =
                    "Request a new code in 00:${countdownSeconds.toString().padStart(2, '0')}"
                Text(
                    text = countdownText,
                    color = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = Nunito,
                )
            }
        }

        Spacer(modifier = Modifier.height(largeGap))

        CustomButton(
            text = "Next",
            onClick = {
                verifyAccountViewModel.onEvent(VerifyAccountFormEvent.Submit)
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
fun ResendCode(isButtonEnabled: Boolean, text: String, onClick: () -> Unit) {
    val textDecoration = if (isButtonEnabled) {
        null
    } else {
        TextDecoration.LineThrough
    }

    Text(
        text = text,
        modifier = Modifier
            .clickable(enabled = isButtonEnabled) {
                if (isButtonEnabled) {
                    onClick()
                }
            },
        style = MaterialTheme.typography.bodyMedium.copy(textDecoration = textDecoration),
        fontFamily = Nunito,
        fontWeight = FontWeight.W800,
        color = if (isButtonEnabled) AzureBlue10 else
            MaterialTheme.colorScheme.onPrimary.copy(0.85f)
    )
}

@Composable
fun EmailHint(email: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                    modifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = Nunito,
                )
            }
        }
    }
}