package com.galacticstudio.digidoro.ui.screens.pomodoro

import com.galacticstudio.digidoro.data.PomodoroModel

data class PomodoroUIState (
    //Get list
    val pomodoroList: List<PomodoroModel> = emptyList(),
    //Create a new one
    val name: String = "",
    val nameError: String? = null,
    val sessionsCompleted: Int = 0,
    val sessionsCompletedError: String? = null,
    val totalSessions: Int = 0,
    val totalSessionsError: String? = null,
    //Pomodoro timer
)