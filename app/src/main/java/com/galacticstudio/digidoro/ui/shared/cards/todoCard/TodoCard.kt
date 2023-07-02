package com.galacticstudio.digidoro.ui.shared.cards.todocard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that represents a Todo Card.
 *
 * @param message The main message to be displayed in the card.
 * @param modifier The modifier for the card. Default is [Modifier].
 * @param boldSubtitle The bold subtitle text to be displayed in the card.
 * @param normalSubtitle The normal subtitle text to be displayed in the card.
 * @param colorTheme The color theme of the card.
 * @param onClick The callback to be invoked when the card is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    message: String,
    modifier: Modifier = Modifier,
    boldSubtitle: String = "",
    normalSubtitle: String = "",
    colorTheme: Color,
    onClick: () -> Unit,
) {
    val borderWidth = 1.5.dp
    val borderRadius = 7.dp
    val paddingCard = 18.dp

    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .border(borderWidth, MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(borderRadius))
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(borderRadius))
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row {
            Box(
                Modifier
                    .width(13.5.dp)
                    .fillMaxHeight()
                    .background(colorTheme)
            ) {
                Spacer(modifier = Modifier.fillMaxWidth())
                Text("")
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.75.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        start = paddingCard,
                        end = paddingCard,
                        top = paddingCard,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    text = message,
                    style = TextStyle(
                        fontSize = 15.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Row {
                        Text(
                            text = boldSubtitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.W800,
                        )
                        Text(
                            text = normalSubtitle,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }

    }
}
