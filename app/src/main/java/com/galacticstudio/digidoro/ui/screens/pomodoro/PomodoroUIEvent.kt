package com.galacticstudio.digidoro.ui.screens.pomodoro

import com.galacticstudio.digidoro.data.PomodoroModel

sealed class PomodoroUIEvent {
    data class SelectedPomodoroChanged(val pomodoro: PomodoroModel): PomodoroUIEvent()
    data class TypeChanged(val type: PomodoroTimerState): PomodoroUIEvent()
    data class EditedChanged(val edited: Boolean, val pomodoro: PomodoroModel?): PomodoroUIEvent()
    object Rebuild: PomodoroUIEvent()
    object ClearData: PomodoroUIEvent()
    object ClearAllData: PomodoroUIEvent()
    data class NameChanged(val name: String): PomodoroUIEvent()
    data class SessionsCompletedChanged(val sessionsCompleted: Int): PomodoroUIEvent()
    data class TotalSessionsChanged(val totalSessions: Int): PomodoroUIEvent()
    data class ThemeChanged(val color: String): PomodoroUIEvent()
    object SavePomodoro: PomodoroUIEvent()
    object UpdatePomodoro: PomodoroUIEvent()
    object EditPomodoro: PomodoroUIEvent()
    object DeletePomodoro: PomodoroUIEvent()
}