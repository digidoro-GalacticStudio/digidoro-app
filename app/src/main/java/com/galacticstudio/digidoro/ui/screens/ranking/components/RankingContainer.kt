package com.galacticstudio.digidoro.ui.screens.ranking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.ui.shared.cards.rankingcard.RankingCard
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.shadowWithCorner

/**
 * Composable function for displaying a ranking container.
 *
 * @param username The username.
 * @param levelName The name of the level.
 * @param currentScore The current score.
 * @param nextLevelScore The score required to reach the next level.
 * @param index The index value.
 */
@Composable
fun RankingContainer(
    username: String,
    levelName: String,
    currentScore: Int,
    nextLevelScore: Int,
    modifier: Modifier = Modifier,
    index: Int = 0,
    cardSmall: Boolean = true,
) {
    Box(
        contentAlignment = Alignment.TopEnd,
    ) {
        RankingCard(
            username = username,
            levelName = levelName,
            currentScore = currentScore,
            nextLevelScore = nextLevelScore,
            modifier = modifier,
            cardSmall = cardSmall,
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .offset((-30).dp, (-10).dp)
                .align(Alignment.TopEnd)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadowWithCorner(
                        cornerRadius = 55.dp,
                        shadowOffset = Offset(8f, 0f),
                        shadowColor = MaterialTheme.colorScheme.onPrimary
                    )
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "#",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.W700,
                    )
                    Text(
                        text = index.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.W800,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset((-20).dp, (-25).dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.awesome_icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
        }
    }
}