package com.galacticstudio.digidoro.ui.screens.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme

@Composable
fun Greeting() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tus pomos",
            modifier = Modifier,
        )

        Text(
            text = "Personaliza y crea tus sesiones de estudio",
            modifier = Modifier
        )

        Box(
            modifier = Modifier
                .background(
                    Color(0xFFE15A51),
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(vertical = 6.dp, horizontal = 25.dp )
        )
        {
            Row {
                Text(
                    text = "Pomo",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    color = Color.White,

                    )

                Text(
                    text = "Short Break",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    color = Color.White
                )

                Text(
                    text = "Long Break",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DigidoroTheme {
        Greeting()
    }
}