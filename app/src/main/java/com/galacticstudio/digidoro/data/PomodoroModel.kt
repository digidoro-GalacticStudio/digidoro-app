package com.galacticstudio.digidoro.data

//TODO Complete all attributes
data class PomodoroModel(
    val name: String = "New Pomodoro",
    val sessionsCompleted: Int,
    val totalSessions: Int,
    val theme: String = "#FFFFFF",
)