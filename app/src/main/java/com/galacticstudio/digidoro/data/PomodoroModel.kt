package com.galacticstudio.digidoro.data

import java.util.Date

data class PomodoroModel(
    val id: String,
    val userId: String,
    val name: String = "New Pomodoro",
    val sessionsCompleted: Int,
    val totalSessions: Int,
    val theme: String = "#FFFFFF",
    val createdAt: Date,
    val updatedAt: Date,
)