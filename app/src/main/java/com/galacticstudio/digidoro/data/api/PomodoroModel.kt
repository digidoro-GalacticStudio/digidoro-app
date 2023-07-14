package com.galacticstudio.digidoro.data.api

import java.util.Calendar
import java.util.Date

data class PomodoroModel(
    val id: String,
    val userId: String = "",
    val name: String = "New Pomodoro",
    val sessionsCompleted: Int,
    val totalSessions: Int,
    val theme: String = "#FFFFFF",
    val createdAt: Date = Calendar.getInstance().time,
    val updatedAt: Date = Calendar.getInstance().time,
): EntityModel()