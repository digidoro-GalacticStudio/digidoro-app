package com.galacticstudio.digidoro.ui.screens.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.ui.screens.pomodoro.components.PomodoroDialog
import com.galacticstudio.digidoro.ui.shared.button.CustomButton
import com.galacticstudio.digidoro.ui.shared.button.ToggleButton
import com.galacticstudio.digidoro.ui.shared.button.ToggleButtonOption
import com.galacticstudio.digidoro.ui.shared.button.ToggleButtonOptionType
import com.galacticstudio.digidoro.ui.shared.cards.pomodoroTimer.DataTime
import com.galacticstudio.digidoro.ui.shared.cards.pomodoroTimer.PomodoroTimer
import com.galacticstudio.digidoro.ui.shared.cards.todocard.TodoCard
import com.galacticstudio.digidoro.ui.shared.titles.CustomMessageData
import com.galacticstudio.digidoro.ui.shared.titles.Title
import com.galacticstudio.digidoro.ui.theme.Cherry_accent
import com.galacticstudio.digidoro.util.DateUtils

@Composable
fun PomodoroScreen(
    navController: NavController,
) {
    var openDialog = rememberSaveable { mutableStateOf(false) }

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 120.dp)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        item {
            Title(
                message = CustomMessageData(
                    title = "Your pomos",
                    subTitle = "Customize and create your study sessions."
                ),
                alignment = Alignment.CenterHorizontally,
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToggleButton(
                    backgroundColor = Cherry_accent,
                    optionList = arrayOf(
                        ToggleButtonOption(ToggleButtonOptionType.Today, "Pomo", null),
                        ToggleButtonOption(ToggleButtonOptionType.Weekly, "Short Break", null),
                        ToggleButtonOption(ToggleButtonOptionType.Monthly, "Long Break", null),
                    )
                ) { selectedOption ->
//                    rankingViewModel.onEvent(RankingUIEvent.ResultTypeChange(selectedOption.type))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PomodoroTimer(dataTime = DataTime(minutes = 25, seconds = 0))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Acción del primer botón */ },
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.skip_next_icon),
                        contentDescription = "reset pomodoro timer",
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(
                    onClick = { /* Acción del primer botón */ },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.play_arrow_icon),
                        contentDescription = "start pomodoro timer",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }

                IconButton(
                    onClick = { /* Acción del tercer botón */ },
                    modifier = Modifier
                        .padding(start = 4.dp)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.play_arrow_icon),
                        contentDescription = "next session",
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.widthIn(0.dp, 120.dp)) {
                    CustomButton(
                        text = "Add",
                    ) {
                        openDialog.value = true
//                    loginViewModel.onEvent(LoginFormEvent.Submit)
                    }
                }

            }
            Spacer(modifier = Modifier.height(24.dp))

            if (openDialog.value) {
                PomodoroDialog {
                    openDialog.value = false
                }
            }

        }

        item {
            Title(
                message = CustomMessageData(
                    title = "Sessions",
                    subTitle = "View your completed study sessions."
                ),
                titleStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

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