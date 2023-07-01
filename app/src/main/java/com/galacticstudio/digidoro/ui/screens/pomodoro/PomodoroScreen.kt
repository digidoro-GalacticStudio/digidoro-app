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
import com.galacticstudio.digidoro.service.StopwatchState
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

//    val stopwatchState = remember { mutableStateOf(StopwatchState()) }
//
//    LaunchedEffect(Unit) {
//        // Moving the service to background when the app is visible
//        moveToBackground(context)
//    }
//
//    val statusReceiver = remember {
//        object : BroadcastReceiver() {
//            @SuppressLint("SetTextI18n")
//            override fun onReceive(p0: Context?, p1: Intent?) {
//                val isRunning = p1?.getBooleanExtra(TimerService.IS_STOPWATCH_RUNNING, false)!!
//                stopwatchState.value.isStopwatchRunning = isRunning
//                val timeElapsed = p1.getIntExtra(TimerService.TIME_ELAPSED, 0)
//                stopwatchState.value.timeElapsed = timeElapsed
//            }
//        }
//    }
//
//    val timeReceiver = remember {
//        object : BroadcastReceiver() {
//            override fun onReceive(p0: Context?, p1: Intent?) {
//                val timeElapsed = p1?.getIntExtra(TimerService.TIME_ELAPSED, 0)!!
//                stopwatchState.value.timeElapsed = timeElapsed
//            }
//        }
//    }
//
//    DisposableEffect(Unit) {
//        val statusFilter = IntentFilter(TimerService.STOPWATCH_STATUS)
//        context.registerReceiver(statusReceiver, statusFilter)
//
//        val timeFilter = IntentFilter(TimerService.STOPWATCH_TICK)
//        context.registerReceiver(timeReceiver, timeFilter)
//
//        onDispose {
//            context.unregisterReceiver(statusReceiver)
//            context.unregisterReceiver(timeReceiver)
//
//            // Moving the service to foreground when the app is in background / not visible
//            moveToForeground(context)
//        }
//    }
//


    val openDialog = rememberSaveable { mutableStateOf(false) }

    val contentPadding = PaddingValues(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 120.dp)

//    val remainingTime by pomodoroViewModel.remainingTime.collectAsState()
//    val isRunning by pomodoroViewModel.isRunning.collectAsState()

    // Observe the remaining time from the ViewModel


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


//        item {
//            Text(
//                text = formatStopwatchTime(stopwatchState.value.timeElapsed),
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }

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
//                val (minutes, seconds) = pomodoroViewModel.remainingTime.value.toFormattedTime()
//                PomodoroTimer(
//                    dataTime = DataTime(
//                        minutes = remainingTime.minutes,
//                        seconds = remainingTime.seconds
//                    )
//                )
                Text(
                    text = "This is the code $hours $minutes $seconds",
                    style = MaterialTheme.typography.bodyLarge
                )
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

//                val handleActionWhenCountdownZero: ActionWhenCountdownZero = object : ActionWhenCountdownZero {
//                    override fun run() {
//                        Toast.makeText(context, "Contador llegó a cero", Toast.LENGTH_SHORT).show()
//                    }
//                }

//                val handleActionWhenCountdownZero: CountdownAction = object : CountdownAction {
//                    override fun run() {
//                        Toast.makeText(context, "Contador llegó a cero", Toast.LENGTH_SHORT).show()
//                    }
//                }


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
                            action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                            else ACTION_SERVICE_START
                        )


                        //Play / pause pomodoro timer
                        if (currentState == StopwatchState.Started) {
                            Log.d("Stopwatch", "Stop     in the view")
//                            pauseStopwatch(context)

//                            ServiceHelper.triggerForegroundService(
//                                context = context,
//                                action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
//                                else ACTION_SERVICE_START
//                            )
                        } else {
                            Log.d("Stopwatch", "Start in the view")
//                            startStopwatch(context)
                        }
//                        if (isRunning) {
//                            pomodoroViewModel.pauseTimer()
//                        } else {
//                            pomodoroViewModel.startTimer()
//                        }
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    val painter = if (currentState == StopwatchState.Started) {
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


//
//@SuppressLint("DefaultLocale")
//@Composable
//fun formatStopwatchTime(timeElapsed: Int): String {
//    val hours: Int = (timeElapsed / 60) / 60
//    val minutes: Int = timeElapsed / 60
//    val seconds: Int = timeElapsed % 60
//    return "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
//}
//
//fun startStopwatch(context: Context) {
//    val timerService = Intent(context, TimerService::class.java)
//    timerService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.START)
//    ContextCompat.startForegroundService(context, timerService)
//}
//
//fun pauseStopwatch(context: Context) {
//    val timerService = Intent(context, TimerService::class.java)
//    timerService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.PAUSE)
//    context.startService(timerService)
//}
//
//fun resetStopwatch(context: Context) {
//    val timerService = Intent(context, TimerService::class.java)
//    timerService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.RESET)
//    context.startService(timerService)
//}
//
//fun moveToForeground(context: Context) {
//    val timerService = Intent(context, TimerService::class.java)
//    timerService.putExtra(
//        TimerService.STOPWATCH_ACTION,
//        TimerService.MOVE_TO_FOREGROUND
//    )
//    context.startService(timerService)
//}
//
//fun moveToBackground(context: Context) {
//    val timerService = Intent(context, TimerService::class.java)
//    timerService.putExtra(
//        TimerService.STOPWATCH_ACTION,
//        TimerService.MOVE_TO_BACKGROUND
//    )
//    context.startService(timerService)
//}
//
//data class StopwatchState(
//    var isStopwatchRunning: Boolean = false,
//    var timeElapsed: Int = 0
//)


//fun DataTime.toFormattedTime(): Pair<String, String> {
//    val minutesString = minutes.toString().padStart(2, '0')
//    val secondsString = seconds.toString().padStart(2, '0')
//
//    return Pair(minutesString, secondsString)
//}

//class PomodoroServiceConnection : ServiceConnection {
//
//    private var service: PomodoroService? = null
//
//    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
//        val pomodoroBinder = binder as PomodoroService.PomodoroBinder
//        service = pomodoroBinder.getService()
//        // Actualiza el estado del temporizador en tu ViewModel o donde lo necesites
//        pomodoroViewModel.updateTimerState(service?.isRunning ?: false)
//    }
//
//    override fun onServiceDisconnected(name: ComponentName?) {
//        service = null
//    }
//
//    fun getService(): PomodoroService? {
//        return service
//    }
//}
