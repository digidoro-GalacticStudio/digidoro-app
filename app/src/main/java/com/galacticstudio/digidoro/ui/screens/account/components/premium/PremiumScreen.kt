package com.galacticstudio.digidoro.ui.screens.account.components.premium

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.screens.login.Logo
import com.galacticstudio.digidoro.ui.screens.login.RegisterButton
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.Nunito

@Composable
fun PremiumScreen(
    navController: NavController,
    premiumViewModel: PremiumViewModel = viewModel(factory = PremiumViewModel.Factory),
) {
    val app: Application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current // Retrieves the current context from the LocalContext.

    LaunchedEffect(key1 = context) {
        // Collect the response events from the RegisterViewModel
        premiumViewModel.responseEvents.collect { event ->
            when (event) {
                is PremiumResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is PremiumResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is PremiumResponseState.Success -> {
                    navController.popBackStack()

                    val roles = app.getRoles().toMutableList()
                    roles.add("premium")
                    app.saveRoles(roles)

                    Toast.makeText(
                        context,
                        "Successfully upgraded",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(35.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Logo()
        Spacer(modifier = Modifier.width(24.dp))

        Title(
            message = CustomMessageData(
                title = "Upgrade to Premium",
                subTitle = "Unlock exclusive features and benefits (free)*"
            ),
            alignment = Alignment.CenterHorizontally
        )
        Spacer(modifier = Modifier.width(24.dp))

        if (app.getRoles().contains("premium")) {
            Text(
                text = "You are already a premium user",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = Nunito,
                color = MaterialTheme.colorScheme.tertiary,
            )
        } else {
            CustomButton(
                text = "Upgrade to Premium",
                onClick = {
                    premiumViewModel.onEvent(PremiumUIEvent.Upgrade)
                }
            )
        }

        Spacer(modifier = Modifier.width(24.dp))
        RegisterButton(
            text = "return",
            onClick = { navController.popBackStack() }
        )
    }

    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
        )
    }
}