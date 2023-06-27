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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.theme.Nunito

/**
 * Composable function for displaying a ranking card.
 *
 * @param username The username.
 * @param levelName The name of the level.
 * @param currentScore The current score.
 * @param nextLevelScore The score required to reach the next level.
 * @param modifier The modifier for styling the card.
 */
@Composable
fun RankingCard(
    username: String,
    levelName: String,
    currentScore: Int,
    nextLevelScore: Int,
    modifier: Modifier = Modifier,
    modifierCard: Modifier = Modifier,
) {
    val borderWidth = 1.dp
    val gap = 7.dp

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .padding(start = 13.dp, top = 10.dp, end = 13.dp, bottom = 13.dp)
            .then(modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .then(modifierCard),
            contentAlignment = Alignment.TopEnd
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.fire_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = username,
                        fontWeight = FontWeight.W800,
                        fontSize = 20.sp,
                        fontFamily = Nunito,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    Text(
                        text = stringResource(R.string.level_text_card),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = Nunito,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = levelName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = Nunito,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Text(
                            text = " ${currentScore}/${nextLevelScore} XP",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W700,
                            fontFamily = Nunito,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }

                    Spacer(modifier = Modifier.height(gap))

                    LinearProgressIndicator(
                        progress = currentScore.toFloat() / nextLevelScore.toFloat(),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth(0.7f),
                    )
                }
            }
        }
    }
}