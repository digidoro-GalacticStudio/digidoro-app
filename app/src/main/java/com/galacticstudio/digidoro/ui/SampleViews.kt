package com.galacticstudio.digidoro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.galacticstudio.digidoro.navigation.AUTH_GRAPH_ROUTE

@Composable
fun NoteScreen(
    navController: NavHostController
) {
    Column {
        Text(text = "Hello - dL", style = MaterialTheme.typography.displayLarge)
        Text(text = "Hello - dMedium", style = MaterialTheme.typography.displayMedium)
        Text(text = "Hello - displaySmall", style = MaterialTheme.typography.displaySmall)
        Text(text = "Hello - headlineLarge", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Hello - headlineMedium",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Hello - headlineSmall", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Hello - titleLarge", style = MaterialTheme.typography.titleLarge)
        Text(text = "Hello - titleMedium", style = MaterialTheme.typography.titleMedium)
        Text(text = "Hello - bodyLarge", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Hello - titleSmall", style = MaterialTheme.typography.titleSmall)
        Text(text = "Hello - labelLarge", style = MaterialTheme.typography.labelLarge)
        Text(text = "Hello - bodyMedium", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Hello - labelMedium", style = MaterialTheme.typography.labelMedium)
        Text(text = "Hello - bodySmall", style = MaterialTheme.typography.bodySmall)
        Text(text = "Hello - labelSmall", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PomodoroScreen(
    navController: NavHostController
) {
    Sample("This is the POMODORO screen",
        onClick = { navController.navigate(AUTH_GRAPH_ROUTE) }
    )
}

@Composable
fun AccountScreen(
    navController: NavController
) {
    Sample("Login",
        onClick = { navController.navigate(AUTH_GRAPH_ROUTE) }
    )
}

@Composable
fun Sample(
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.Magenta,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    onClick()
                },
        )
    }
}
