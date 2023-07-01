package com.galacticstudio.digidoro.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import com.galacticstudio.digidoro.MainActivity
import com.galacticstudio.digidoro.util.Service.CANCEL_REQUEST_CODE
import com.galacticstudio.digidoro.util.Service.CLICK_REQUEST_CODE
import com.galacticstudio.digidoro.util.Service.RESUME_REQUEST_CODE
import com.galacticstudio.digidoro.util.Service.TIMER_STATE
import com.galacticstudio.digidoro.util.Service.STOP_REQUEST_CODE

/**
 * Helper class for managing pending intents and triggering foreground services related to the TimerService.
 *
 * Note: This class is annotated with @ExperimentalAnimationApi, which means it includes experimental APIs
 * related to animation. Use it with caution as APIs marked as experimental might change in the future.
 */
@ExperimentalAnimationApi
object ServiceHelper {

    // Define a flag to be used based on the device's Android version
    private val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            0

    /**
     * Create a PendingIntent for the "click" action to start the MainActivity with the TimerState set to "Started".
     *
     * @param context The application context.
     * @return The PendingIntent for the "click" action.
     */
    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    /**
     * Create a PendingIntent for the "stop" action to stop the TimerService with the TimerState set to "Stopped".
     *
     * @param context The application context.
     * @return The PendingIntent for the "stop" action.
     */
    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    /**
     * Create a PendingIntent for the "resume" action to resume the TimerService with the TimerState set to "Started".
     *
     * @param context The application context.
     * @return The PendingIntent for the "resume" action.
     */
    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    /**
     * Create a PendingIntent for the "cancel" action to cancel the TimerService with the TimerState set to "Canceled".
     *
     * @param context The application context.
     * @return The PendingIntent for the "cancel" action.
     */
    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    /**
     * Trigger the TimerService as a foreground service with the specified action.
     *
     * @param context The application context.
     * @param action The action to be performed by the TimerService.
     */
    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, TimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    /**
     * Trigger the TimerService as a foreground service with the specified action and initialSeconds.
     *
     * @param context The application context.
     * @param action The action to be performed by the TimerService.
     * @param initialSeconds The initial seconds to be passed to the TimerService (optional).
     */
    fun triggerForegroundService(context: Context, action: String, initialSeconds: Int? = null) {
        Intent(context, TimerService::class.java).apply {
            this.action = action
            initialSeconds?.let { putExtra("initialSeconds", it) }
            context.startService(this)
        }
    }
}