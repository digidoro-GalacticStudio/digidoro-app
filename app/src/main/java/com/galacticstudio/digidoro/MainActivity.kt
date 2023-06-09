package com.galacticstudio.digidoro

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.galacticstudio.digidoro.navigation.AppScaffold
import com.galacticstudio.digidoro.service.TimerListener
import com.galacticstudio.digidoro.service.TimerService
import com.galacticstudio.digidoro.ui.screens.MainViewModel
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroUIEvent
import com.galacticstudio.digidoro.ui.screens.pomodoro.viewmodel.PomodoroViewModel
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.work.NetworkChangeHandler

@ExperimentalAnimationApi
class MainActivity : ComponentActivity(), TimerListener {
    private lateinit var timerService: TimerService
    private lateinit var networkChangeHandler: NetworkChangeHandler

    // Variable to indicate if the service is bound
    private var isBound by mutableStateOf(false)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isBound = true
            timerService.setTimerListener(this@MainActivity)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    // Get an instance of the ViewModel
    private val pomodoroViewModel: PomodoroViewModel by viewModels {
        PomodoroViewModel.Factory
    }

    // Method called when 0:00 minute is reached on the stopwatch
    override fun onTimeReached(pomodoroId: String, type: String) {
        if (pomodoroId.isNotEmpty() && type == "pomodoro") {
            val auxiliarViewModel: PomodoroViewModel by viewModels {
                PomodoroViewModel.Factory
            }
            auxiliarViewModel.onEvent(PomodoroUIEvent.IncrementSessionPomodoro(pomodoroId))
        }

        // Play the notification sound
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()

        // Get an instance of the Vibrator
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check if the device has vibration capability
        if (vibrator.hasVibrator()) {
            // Check the Android version to use the appropriate method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(1000)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Bind to the TimerService
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        networkChangeHandler.registerNetworkCallback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkChangeHandler = NetworkChangeHandler(this)

        setContent {
            val controller = rememberNavController()

            val mainViewModel: MainViewModel by viewModels {
                MainViewModel.Factory(applicationContext)
            }

            DigidoroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(isBound) {
                        AppScaffold(
                            navController = controller,
                            mainViewModel = mainViewModel,
                            stopwatchService = timerService,
                            pomodoroViewModel = pomodoroViewModel,
                        )
                    }
                }
            }
        }
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("App", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    override fun onStop() {
        super.onStop()

        // Unbind from the service and set isBound to false
        unbindService(connection)
        isBound = false
    }
}
