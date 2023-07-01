package com.galacticstudio.digidoro.ui.screens.pomodoro

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.todoList
import com.galacticstudio.digidoro.service.ServiceHelper
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.service.TimerState
import com.galacticstudio.digidoro.ui.screens.pomodoro.components.PomodoroDialog
import com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel.PomodoroViewModel
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
import com.galacticstudio.digidoro.util.Service
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_CANCEL
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_START
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_STOP

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomodoroScreen(
    navController: NavController,
    pomodoroViewModel: PomodoroViewModel = viewModel(factory = PomodoroViewModel.Factory),
    stopwatchService: TimerService,
) {
    val context = LocalContext.current
    val hours by stopwatchService.hours
    val minutes by stopwatchService.minutes
    val seconds by stopwatchService.seconds
    val currentState by stopwatchService.currentState

    LaunchedEffect(Unit) {
        snapshotFlow { minutes.toInt() to seconds.toInt() }
            .collect { (min, sec) ->
                if (min == 0 && sec == 0) {
                    // Do something when both minutes and seconds reach 0
                    // For example, stop the stopwatch or navigate to another screen
                    Toast.makeText(context, "Contador llegó a cero", Toast.LENGTH_SHORT).show()
                }
            }
    }

    val openDialog = rememberSaveable { mutableStateOf(false) }

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
                PomodoroTimer(
                    dataTime = DataTime(
                        minutes = minutes,
                        seconds = seconds
                    )
                )
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
                    onClick = {
                        //Reset the pomodoro timer
//                        pomodoroViewModel.resetTimer()
//                        resetStopwatch(context)
                        ServiceHelper.triggerForegroundService(
                            context = context, action = ACTION_SERVICE_CANCEL
                        )
                    },
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

                var number = 0
                IconButton(
                    onClick = {

                        if (number == 0) {
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = Service.ACTION_SERVICE_VARIABLES,
                                initialSeconds = 1,
//                                handleActionWhenCountdownZero = handleActionWhenCountdownZero
                            )
                            number = 2
                        }
//

                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = if (currentState == TimerState.Started) ACTION_SERVICE_STOP
                            else ACTION_SERVICE_START
                        )
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    val painter = if (currentState == TimerState.Started) {
                        R.drawable.pause_icon
                    } else {
                        R.drawable.play_arrow_icon
                    }
                    Icon(
                        painter = painterResource(painter),
                        contentDescription = "start pomodoro timer",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }

                IconButton(
                    onClick = {
                        //Skip pomodoro sessions
//                        pomodoroViewModel.resetTimer()
                        //TODO
                    },
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