package com.galacticstudio.digidoro.ui.screens.pomodoro

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.galacticstudio.digidoro.service.ServiceHelper
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.service.TimerState
import com.galacticstudio.digidoro.ui.screens.pomodoro.components.PomodoroDialog
import com.galacticstudio.digidoro.ui.screens.pomodoro.components.PomodoroRichTooltip
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
import com.galacticstudio.digidoro.util.Service
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_CANCEL
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_START
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_STOP
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
        pomodoroViewModel.onEvent(PomodoroUIEvent.Rebuild)

        ServiceHelper.triggerForegroundService(
            context = context,
            action = Service.ACTION_SERVICE_VARIABLES,
            initialMinutes = 1,
            type = "pomodoro"
        )

        snapshotFlow { minutes.toInt() to seconds.toInt() }
            .collect { (min, sec) ->
                if (min == 0 && sec == 0) {
                    when (pomodoroViewModel.pomodoroType.value) {
                        PomodoroTimerState.Pomodoro -> {
                            pomodoroViewModel.onEvent(PomodoroUIEvent.Rebuild)
                        }

                        PomodoroTimerState.ShortBreak -> {

                        }

                        PomodoroTimerState.LongBreak -> {

                        }
                    }
                }
            }
    }

    LaunchedEffect(key1 = context) {
        // Collect the response events from the pomodoroViewModel
        pomodoroViewModel.responseEvents.collect { event ->
            when (event) {
                is PomodoroResponseState.Error -> {
                    Toast.makeText(
                        context,
                        "An error has occurred ${event.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is PomodoroResponseState.ErrorWithMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }


    val openDialog = rememberSaveable { mutableStateOf(false) }

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 120.dp)

    //Tooltip variables
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Title(
                    message = CustomMessageData(
                        title = "Your pomos",
                        subTitle = "Customize and create your study sessions."
                    ),
                    alignment = Alignment.CenterHorizontally,
                )

                IconButton(onClick = {scope.launch { tooltipState.show() }} ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Question",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                PomodoroRichTooltip(tooltipState,scope)
            }

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
                    ),
                    enabled = pomodoroViewModel.state.value.selectedPomodoro != null
                ) { selectedOption ->
                    if (pomodoroViewModel.state.value.selectedPomodoro == null) {
                        Toast.makeText(
                            context,
                            "Select o create a new pomodoro",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@ToggleButton
                    }

                    val type = when (selectedOption.type) {
                        is ToggleButtonOptionType.Today -> PomodoroTimerState.Pomodoro
                        is ToggleButtonOptionType.Weekly -> PomodoroTimerState.ShortBreak
                        is ToggleButtonOptionType.Monthly -> PomodoroTimerState.LongBreak
                        else -> {
                            PomodoroTimerState.Pomodoro
                        }
                    }

                    val minutesType = when (selectedOption.type) {
                        is ToggleButtonOptionType.Today -> 1
                        is ToggleButtonOptionType.Weekly -> 2
                        is ToggleButtonOptionType.Monthly -> 3
                        else -> {
                            0
                        }
                    }

                    val typeOption = when (selectedOption.type) {
                        is ToggleButtonOptionType.Today -> "pomodoro"
                        is ToggleButtonOptionType.Weekly -> "short_break"
                        is ToggleButtonOptionType.Monthly -> "long_break"
                        else -> {
                            "pomodoro"
                        }
                    }

                    //The timer was canceled, and I'm resending the notification variables.
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_CANCEL
                    )
                    
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = Service.ACTION_SERVICE_VARIABLES,
                        initialMinutes = minutesType,
                        type = typeOption,
                    )

                    pomodoroViewModel.onEvent(PomodoroUIEvent.TypeChanged(type))
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
                        if (pomodoroViewModel.state.value.selectedPomodoro == null) {
                            Toast.makeText(
                                context,
                                "Select o create a new pomodoro",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

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

                IconButton(
                    onClick = {
                        if (pomodoroViewModel.state.value.selectedPomodoro == null) {
                            Toast.makeText(
                                context,
                                "Select o create a new pomodoro",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

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
                        if (pomodoroViewModel.state.value.selectedPomodoro == null) {
                            Toast.makeText(
                                context,
                                "Select o create a new pomodoro",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }

                        if (pomodoroViewModel.pomodoroType.value == PomodoroTimerState.Pomodoro) {
                            Toast.makeText(
                                context,
                                "You can only skip the breaks",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
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
                    }
                }

            }
            Spacer(modifier = Modifier.height(24.dp))

            if (openDialog.value) {
                PomodoroDialog(pomodoroViewModel) {
                    openDialog.value = false
                }
            }
        }

        if (pomodoroViewModel.state.value.selectedPomodoro != null) {
            val selectedPomodoro = pomodoroViewModel.state.value.selectedPomodoro

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Title(
                    message = CustomMessageData(
                        title = "Active Pomodoro",
                        subTitle = "Begin the timer and conquer your tasks one Pomodoro at a time."
                    ),
                    titleStyle = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                TodoCard(
                    message = selectedPomodoro?.name ?: "",
                    boldSubtitle = "${selectedPomodoro?.sessionsCompleted} / ",
                    normalSubtitle = selectedPomodoro?.totalSessions.toString(),
                    colorTheme = Color(
                        android.graphics.Color.parseColor(
                            selectedPomodoro?.theme.toString()
                        )
                    ),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(24.dp))
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

        itemsIndexed(pomodoroViewModel.state.value.pomodoroList) { _, todo ->
            TodoCard(
                message = todo.name,
                boldSubtitle = "${todo.sessionsCompleted} / ",
                normalSubtitle = todo.totalSessions.toString(),
                colorTheme = Color(android.graphics.Color.parseColor(todo.theme)),
                onClick = {
                    pomodoroViewModel.onEvent(PomodoroUIEvent.SelectedPomodoroChanged(todo))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


enum class PomodoroTimerState {
    Pomodoro,
    LongBreak,
    ShortBreak,
}