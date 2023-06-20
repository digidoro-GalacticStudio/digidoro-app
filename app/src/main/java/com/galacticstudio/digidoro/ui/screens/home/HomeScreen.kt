package com.galacticstudio.digidoro.ui.screens.home

import android.content.res.Configuration
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.data.pomodoroList
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.ui.screens.home.viewmodel.HomeViewModel
import com.galacticstudio.digidoro.ui.shared.cards.pomodoroCard.PomodoroCard
import com.galacticstudio.digidoro.ui.shared.cards.todocard.TodoCard
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.ui.theme.Nunito
import com.galacticstudio.digidoro.util.DateUtils

@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()

    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(navController = navController)
        }
    }
}

/**
 * A composable function representing the home screen.
 *
 * @param navController The NavHostController used for navigation.
 */
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel(),
) {
    val app: RetrofitApplication = LocalContext.current.applicationContext as RetrofitApplication
    val state = homeViewModel.state // Retrieves the current state from the HomeViewModel.

    homeViewModel.onEvent(HomeUIEvent.UsernameChanged(app.getUsername()))

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 75.dp)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
        state = rememberLazyListState()
    ) {
        item {
            WelcomeUser(state)
        }

        // Ranking
        item {
            RankingHome()
        }

        //Pomodoro title
        item {
            Spacer(modifier = Modifier.height(24.dp))

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
            Spacer(modifier = Modifier.height(24.dp))
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
            Spacer(modifier = Modifier.height(32.dp))
            Title(
                message = CustomMessageData(
                    title = "Your Activities",
                    subTitle = "Take a look at your pending tasks."
                ),
                titleStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
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
fun WelcomeUser(
    state: HomeUIState
) {
    Text(
        text = "Hello again,",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        fontFamily = Nunito,
        fontWeight = FontWeight.W800,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.username,
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