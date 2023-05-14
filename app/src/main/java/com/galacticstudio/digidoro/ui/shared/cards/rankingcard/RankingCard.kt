package com.galacticstudio.digidoro.ui.shared.cards.rankingcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.shadowWithBorder


@Composable
fun RankingCard(
    username: String,
    levelName: String,
    currentScore: Int,
    nextLevelScore: Int,
    colorShadow: Color = Color.Yellow
) {
    val borderWidth = 1.dp
    val gap = 7.dp

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderWidth, Color.Black),
        modifier = Modifier
            .padding(16.dp)
            .shadowWithBorder(
                borderWidth = borderWidth,
                borderColor = Color.Black,
                cornerRadius = 11.8825.dp,
                shadowColor = colorShadow,
                shadowOffset = Offset(15f, 15f)
            )
    ) {
        Box(
            modifier = Modifier
                .width(500.dp)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.heart_border_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(start = 24.dp)
                ) {
                    Text(
                        text = username,
                        color = Color.Black,
                        fontWeight = FontWeight.W800,
                        fontSize = 20.sp,
                        fontFamily = Nunito,
                    )

                    Spacer(modifier = Modifier.height(gap * 2))

                    Text(
                        text = "Nivel",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontFamily = Nunito,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = levelName,
                            style = TextStyle(color = Color.Black),
                            fontSize = 14.sp,
                            fontFamily = Nunito,
                        )
                        Text(
                            text = " ${currentScore}/${nextLevelScore} EXP",
                            style = TextStyle(color = Color.Blue),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                            fontFamily = Nunito,
                        )
                    }

                    Spacer(modifier = Modifier.height(gap))

                    LinearProgressIndicator(
                        progress = currentScore.toFloat() / nextLevelScore.toFloat(),
                        color = Color.hsl(220f, 1f, 0.75f),
                        modifier = Modifier.fillMaxWidth(0.7f),
                    )
                }
            }
        }
    }
}