package com.galacticstudio.digidoro.ui.screens.pomodoro

sealed class PomodoroResponseState {
    object Resume : PomodoroResponseState()
    class Error(val exception: Exception) : PomodoroResponseState()
    data class ErrorWithMessage(val message: String) : PomodoroResponseState()
    object Success : PomodoroResponseState()
}