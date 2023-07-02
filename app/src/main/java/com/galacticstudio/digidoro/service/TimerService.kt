package com.galacticstudio.digidoro.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_CANCEL
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_START
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_STOP
import com.galacticstudio.digidoro.util.Service.ACTION_SERVICE_VARIABLES
import com.galacticstudio.digidoro.util.Service.NOTIFICATION_CHANNEL_ID
import com.galacticstudio.digidoro.util.Service.NOTIFICATION_CHANNEL_NAME
import com.galacticstudio.digidoro.util.Service.NOTIFICATION_ID
import com.galacticstudio.digidoro.util.Service.TIMER_STATE
import com.galacticstudio.digidoro.util.formatTime
import com.galacticstudio.digidoro.util.pad
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * A service that provides timer functionality.
 */
@ExperimentalAnimationApi
class TimerService : Service() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = TimerBinder()

    // Initial timer settings
    private var initialTime: Int = 25
    private var duration: Duration = (initialTime.toLong() * 60).seconds
    private var savedDuration: Duration = (initialTime.toLong() * 60).seconds
    private var type: String = "pomodoro"

    private lateinit var timer: Timer

    // Mutable state variables
    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("20")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(TimerState.Idle)
        private set

    /**
     * Binds the service to the client.
     */
    override fun onBind(p0: Intent?) = binder

    /**
     * Sets the timer listener.
     */
    private var timerListener: TimerListener? = null

    fun setTimerListener(listener: TimerListener) {
        timerListener = listener
    }

    /**
     * Initializes the service.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize dependencies manually
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = createNotificationBuilder()
    }

    /**
     * Creates the notification builder for the timer service.
     */
    private fun createNotificationBuilder(): NotificationCompat.Builder {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createPreNotificationChannel()
            else ""

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro - Running")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.lc_igi_digidoro)
            .setOngoing(true)
            .addAction(0, "Stop", ServiceHelper.stopPendingIntent(this))
            .addAction(0, "Cancel", ServiceHelper.cancelPendingIntent(this))
            .setContentIntent(ServiceHelper.clickPendingIntent(this))
    }

    /**
     * Creates the pre-notification channel for Android Oreo and above.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPreNotificationChannel(): String {
        val channelId = NOTIFICATION_CHANNEL_ID
        val channelName = NOTIFICATION_CHANNEL_NAME
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        notificationManager.createNotificationChannel(channel)

        return channelId
    }

    /**
     * Handles the start command for the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(TIMER_STATE)) {
            TimerState.Variables.name -> {

                this.initialTime =  intent.getIntExtra("initialMinutes", 0)
                this.type = intent.getStringExtra("type") ?: "pomodoro"

                duration = (initialTime.toLong() * 60).seconds
                savedDuration = (initialTime.toLong() * 60).seconds

                updateTimeUnits()
            }

            TimerState.Started.name -> {
                setStopButton()
                startForegroundService()

                updateTitleNotification("Running")

                startTimer(savedDuration) { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }

            TimerState.Stopped.name -> {
                stopTimer()
                setResumeButton()
                updateTitleNotification("Paused")
            }

            TimerState.Canceled.name -> {
                stopTimer()
                cancelTimer()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_VARIABLES -> {
                    val initialMinutes = intent?.getIntExtra("initialMinutes", 0)
                    this.initialTime = initialMinutes ?: 0

                    this.type = intent?.getStringExtra("type") ?: "pomodoro"

                    duration = (initialTime.toLong() * 60).seconds
                    savedDuration = (initialTime.toLong() * 60).seconds

                    updateTimeUnits()
                }

                ACTION_SERVICE_START -> {
                    setStopButton()
                    startForegroundService()

                    startTimer(savedDuration) { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }

                    updateTitleNotification("Running")
                }

                ACTION_SERVICE_STOP -> {
                    stopTimer()
                    setResumeButton()
                    savedDuration = duration

                    updateTitleNotification("Paused")
                }

                ACTION_SERVICE_CANCEL -> {
                    stopTimer()
                    cancelTimer()
                    stopForegroundService()
                }

                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }



    /**
     * Starts the timer with the given duration and tick listener.
     */
    private fun startTimer(
        savedDuration: Duration,
        onTick: (h: String, m: String, s: String) -> Unit
    ) {
        currentState.value = TimerState.Started
        duration = savedDuration
        updateTimeUnits()
        onTick(hours.value, minutes.value, seconds.value)

        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            if (duration > Duration.ZERO) {
                duration -= 1.seconds
                updateTimeUnits()
                onTick(hours.value, minutes.value, seconds.value)
            } else {
                stopTimer()

                updateTitleNotification("Finished")
                timerListener?.onTimeReached()

                when (type) {
                    "pomodoro" -> {
                        updateNotification("You've completed your Pomodoro session. Take a short break and recharge!")
                    }

                    "short_break" -> {
                        updateNotification("Start a new Pomodoro session and keep up the momentum.")
                    }

                    "long_break" -> {
                        updateNotification("You're refreshed and ready to go! Start a new Pomodoro session and make progress towards your goals.")
                    }
                }
            }
        }
    }

    /**
     * Stops the timer.
     */
    private fun stopTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = TimerState.Stopped
    }

    /**
     * Cancels the timer and resets the duration.
     */
    private fun cancelTimer() {
        duration = (initialTime.toLong() * 60).seconds
        savedDuration = (initialTime.toLong() * 60).seconds
        currentState.value = TimerState.Idle
        updateTimeUnits()
    }

    /**
     * Updates the time units (hours, minutes, seconds) based on the current duration.
     */
    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@TimerService.hours.value = hours.toInt().pad()
            this@TimerService.minutes.value = minutes.pad()
            this@TimerService.seconds.value = seconds.pad()
        }
    }

    /**
     * Starts the foreground service and shows the notification.
     */
    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    /**
     * Stops the foreground service and removes the notification.
     */
    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Creates the notification channel for Android Oreo and above.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Updates the notification with the given time units (hours, minutes, seconds).
     */
    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                )
            ).build()
        )
    }

    /**
     * Updates the notification with the given title and content.
     */
    private fun updateNotification(message: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText(message)
                .build()
        )
    }

    private fun updateTitleNotification(action: String) {
        val title = when (type) {
            "pomodoro" -> "Pomodoro session - $action"
            "short_break" -> "Short break of the Pomodoro - $action"
            "long_break" -> "Long break of the Pomodoro - $action"
            else -> {"Pomodoro - $action"}
        }

        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentTitle(title)
                .build()
        )
    }

    /**
     * Sets the "Stop" button action in the notification.
     */
    @SuppressLint("RestrictedApi")
    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Stop",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    /**
     * Sets the "Resume" button action in the notification.
     */
    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                ServiceHelper.resumePendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    /**
     * Inner class representing the binder for the timer service.
     */
    inner class TimerBinder : Binder() {
        /**
         * Returns the TimerService instance.
         */
        fun getService(): TimerService = this@TimerService
    }
}

/**
 * Represents the possible states of the timer.
 */
enum class TimerState {
    Idle,
    Variables,
    Started,
    Stopped,
    Canceled
}

/**
 * Listener interface for timer events.
 */
interface TimerListener {
    /**
     * Called when the timer reaches the specified time.
     */
    fun onTimeReached()
}
