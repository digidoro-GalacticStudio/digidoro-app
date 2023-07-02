package com.galacticstudio.digidoro.network.dto.pomodoro

data class PomodoroRequest(
    val name: String,
    val sessions_completed: Int,
    val total_sessions: Int,
    val theme: String
)
