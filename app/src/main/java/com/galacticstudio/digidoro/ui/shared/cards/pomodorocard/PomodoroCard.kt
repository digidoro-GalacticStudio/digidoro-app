package com.galacticstudio.digidoro.ui.shared.cards.pomodorocard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.shadowWithBorder

@Composable
fun PomodoroCard(
    message: String,
    section: String = ""
) {
    val borderRadius = 11.8825.dp
    val borderWidth = 2.dp
    val paddingValue = 18.dp

    Box(
        Modifier
            .zIndex(1f)
            .size(150.dp)
            .shadowWithBorder(
                borderWidth = borderWidth,
                borderColor = Color.Green,
                cornerRadius = borderRadius,
                shadowColor = Color.Red,
                shadowOffset = Offset(15f, 15f)
            )
            .border(borderWidth, Color.Black, shape = RoundedCornerShape(borderRadius))
            .background(Color.White, shape = RoundedCornerShape(borderRadius))
            .padding(start = paddingValue, end = paddingValue, top = paddingValue, bottom = 5.dp)
    ) {
        Text(
            text = message,
            color = Color.Black,
            fontFamily = Nunito,
            style = TextStyle(
                fontSize = 16.sp
            ),
            fontWeight = FontWeight.W800,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(width = 156.dp)
                .height(height = 97.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = section,
                color = Color.Black,
                fontSize = 11.5.sp
            )
        }
    }
}

