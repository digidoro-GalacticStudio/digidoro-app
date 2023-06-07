package com.galacticstudio.digidoro.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.pomodoroList
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.navigation.AppScaffold
import com.galacticstudio.digidoro.ui.shared.cards.pomodorocard.PomodoroCard
import com.galacticstudio.digidoro.ui.shared.cards.todocard.TodoCard
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.DateUtils

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}

/**
 * A composable function representing the home screen.
 *
 * @param navController The NavHostController used for navigation.
 */
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    AppScaffold(
        navController = navController,
        content = {
            HomeContent()
        }
    )
}

/**
 * A composable function representing the content of the home screen.
 */
@Composable
fun HomeContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(35.dp),
        state = rememberLazyListState()
    ) {
        item {
            WelcomeUser()
        }

        // Ranking
        item {
            RankingHome()
        }

        //Pomodoro title
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What did we leave off on?",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = Nunito,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Title(
                message = CustomMessageData(
                    title = "Your Pomodoros",
                    subTitle = "Your recent sessions"
                ),
                titleStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        //Pomodoro List
        item {
            LazyRow(
                state = rememberLazyListState()
            ) {
                itemsIndexed(pomodoroList) { index, pomodoro ->
                    PomodoroCard(
                        message = pomodoro.name,
                        sectionText = "Pomodoro ${index + 1}",
                        colorTheme = Color(android.graphics.Color.parseColor(pomodoro.theme)),
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }

        //Activities title
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Title(
                message = CustomMessageData(
                    title = "Your Activities",
                    subTitle = "Take a look at your pending tasks."
                ),
                titleStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        //Activities list
        itemsIndexed(todoList) { _, todo ->
            val (text, formattedDate) = DateUtils.formatDateWithTime(todo.createdAt)
            TodoCard(
                message = todo.title,
                boldSubtitle = text,
                normalSubtitle = formattedDate,
                colorTheme = Color(android.graphics.Color.parseColor(todo.theme)),
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * A composable function representing the welcome message and user information.
 */
@Composable
fun WelcomeUser() {
    Text(
        text = "Hello again,",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        fontFamily = Nunito,
        fontWeight = FontWeight.W800,
        color = Color(0xFF202124)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        //TODO Get the USER NAME from API
        Text(
            text = "Jenny",
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = Nunito,
            fontWeight = FontWeight.W800,
            color = colorResource(id = R.color.accent_color),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.heart_fill_icon),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
            colorFilter = ColorFilter.tint(Color(0xFFE15A51))
        )
    }
}

/**
 * A composable function representing the ranking section.
 */
@Composable
fun RankingHome() {
    Spacer(modifier = Modifier.width(8.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Image(
            painter = painterResource(R.drawable.ranking_icon),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
        )
        Text(
            text = "ranking",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = Nunito,
        )

        //TODO Get the USER LEVEL and CURRENT SCORE from API
        val levelName = "dreamer"
        val currentScore = 85
        //TODO Calculate the next level score using the level name
        val nextLevelScore = 255

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = levelName,
                style = TextStyle(color = Color.Black),
                fontSize = 14.sp,
                fontFamily = Nunito,
            )
            Text(
                text = " ${currentScore}/${nextLevelScore} EXP",
                style = TextStyle(color = colorResource(id = R.color.accent_color)),
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                fontFamily = Nunito,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = currentScore.toFloat() / nextLevelScore.toFloat(),
            color = Color(0xFF4981FF),
            modifier = Modifier.width(140.dp),
        )
    }
}