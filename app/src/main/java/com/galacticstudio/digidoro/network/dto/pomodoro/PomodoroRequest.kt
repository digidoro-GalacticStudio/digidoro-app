package com.galacticstudio.digidoro.network.dto.pomodoro

data class PomodoroRequest(
    val name: String,
    val sessionsCompleted: Int,
    val totalSessions: Int,
    val theme: String
)
