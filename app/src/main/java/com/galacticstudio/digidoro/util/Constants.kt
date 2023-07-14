package com.galacticstudio.digidoro.util

import androidx.compose.ui.unit.dp

object NetworkService {
    const val BASE_URL = "https://digidoro.rocks/"
}

object WindowSize {
    val COMPACT = 600.dp
    val MEDIUM = 840.dp
    val EXPANDED = 1200.dp
}

object Service {
    const val ACTION_SERVICE_VARIABLES = "ACTION_SERVICE_VARIABLES"
    const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
    const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"
    const val ACTION_SERVICE_CANCEL = "ACTION_SERVICE_CANCEL"

    const val TIMER_STATE = "TIMER_STATE"

    const val NOTIFICATION_CHANNEL_ID = "TIMER_NOTIFICATION_ID"
    const val NOTIFICATION_CHANNEL_NAME = "TIMER_NOTIFICATION"
    const val NOTIFICATION_ID = 10

    const val CLICK_REQUEST_CODE = 100
    const val CANCEL_REQUEST_CODE = 101
    const val STOP_REQUEST_CODE = 102
    const val RESUME_REQUEST_CODE = 103
}