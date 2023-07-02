package com.galacticstudio.digidoro.ui.shared.cards.pomodoroCard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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

/**
 * A composable function that represents a Pomodoro Card.
 *
 * @param message The main message to be displayed in the card.
 * @param modifier The modifier for the card. Default is [Modifier].
 * @param sectionText The additional section text to be displayed in the card.
 * @param colorTheme The color theme of the card. Default is [Color.Red].
 * @param onClick The callback to be invoked when the card is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroCard(
    message: String,
    modifier: Modifier = Modifier,
    sectionText: String = "",
    colorTheme: Color = Color.Red,
    onClick: () -> Unit,
) {
    val borderRadius = 11.8825.dp
    val borderWidth = 2.dp
    val paddingValue = 18.dp

    Card(
        onClick = { onClick() },
        modifier = Modifier
            .zIndex(1f)
            .padding(end = 7.dp)
            .width(140.dp)
            .height(135.dp)
            .shadowWithBorder(
                borderWidth = 4.dp,
                borderColor = MaterialTheme.colorScheme.onPrimary,
                cornerRadius = borderRadius,
                shadowColor = colorTheme,
                shadowOffset = Offset(15f, 15f)
            )
            .border(
                borderWidth,
                MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(borderRadius)
            )
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = paddingValue,
                    end = paddingValue,
                    top = paddingValue,
                    bottom = 0.dp
                )
                .fillMaxSize(),
        ) {
            Text(
                text = message,
                fontFamily = Nunito,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                fontWeight = FontWeight.W800,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = sectionText,
                    fontSize = 11.5.sp
                )
            }
        }
    }
}
