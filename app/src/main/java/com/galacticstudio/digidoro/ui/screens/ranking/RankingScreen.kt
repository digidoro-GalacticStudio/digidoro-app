package com.galacticstudio.digidoro.ui.screens.ranking

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.userList
import com.galacticstudio.digidoro.ui.screens.ranking.components.RankingContainer
import com.galacticstudio.digidoro.ui.shared.cards.rankingcard.RankingCard
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.util.shadowWithBorder

@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RankingScreenPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RankingScreen(navController = navController)
        }
    }
}

/**
 * Composable function for displaying the ranking screen.
 *
 * @param navController The navigation controller.
 */
@Composable
fun RankingScreen(
    navController: NavHostController
) {
    val borderRadius = 28.dp

    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = null,
            modifier = Modifier.size(25.dp),
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(35.dp),
    ) {
        item {
            Title(
                message = CustomMessageData(
                    title = stringResource(R.string.ranking_screen_title),
                    subTitle = stringResource(R.string.ranking_screen_statement)
                ),
                alignment = Alignment.CenterHorizontally
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            val myUser = userList[1]
            RankingCard(
                username = myUser.name,
                levelName = myUser.level,
                currentScore = myUser.total_score,
                nextLevelScore = 2000,
                modifier = Modifier.shadowWithBorder(
                    borderWidth = 2.dp,
                    borderColor = MaterialTheme.colorScheme.onPrimary,
                    cornerRadius = 16.dp,
                    shadowColor = MaterialTheme.colorScheme.onTertiary,
                    shadowOffset = Offset(15f, 15f)
                )
            )
        }

        //TODO Implement the segmented button

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Title(
                message = CustomMessageData(
                    title = stringResource(R.string.ranking_screen_subtitle),
                    subTitle = stringResource(R.string.ranking_screen_score)
                ),
                titleStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowWithBorder(
                        borderWidth = 2.dp,
                        borderColor = MaterialTheme.colorScheme.onPrimary,
                        cornerRadius = borderRadius,
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        shadowOffset = Offset(15f, 1f)
                    )
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(borderRadius)
                    )
                    .clip(RoundedCornerShape(borderRadius))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(borderRadius)
                    )
                    .padding(vertical = 28.dp)
            ) {
                userList.forEachIndexed { index, user ->
                    RankingContainer(
                        username = user.name,
                        levelName = user.level,
                        currentScore = user.total_score,
                        nextLevelScore = 2000,
                        index = index + 1,
                    )
                }
            }
        }
    }
}