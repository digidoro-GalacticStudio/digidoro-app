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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TodoCard(
    message: String,
    boldSubtitle: String = "",
    normalSubtitle: String = "",
) {
    val borderWidth = 2.dp
    val borderRadius = 11.8825.dp
    val paddingCard = 18.dp

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(borderWidth, Color.Black, shape = RoundedCornerShape(borderRadius))
            .height(IntrinsicSize.Min)
    ) {
        Box(
            Modifier
                .width(13.5.dp)
                .fillMaxHeight()
                .background(
                    Color.Green,
                    shape = RoundedCornerShape(borderRadius + 25.dp, 0.dp, 0.dp, borderRadius)
                )
        ) {
            Spacer(modifier = Modifier.fillMaxWidth())
            Text("")
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(borderWidth),
            color = Color.Black
        )
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = paddingCard, end = paddingCard, top = paddingCard, bottom = 8.dp)
        ) {
            Text(
                text = message,
                color = Color.Black,
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
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.W800,
                    )
                    Text(
                        text = normalSubtitle,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}