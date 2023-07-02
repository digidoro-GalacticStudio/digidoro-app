package com.galacticstudio.digidoro.ui.screens.home

import com.galacticstudio.digidoro.data.api.PomodoroModel
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.api.UserRankingModel

data class HomeUIState(
    val username: String = "",
    val user: UserRankingModel? = null,
    var todos: List<TodoModel> = emptyList(),
    var pomodoros: List<PomodoroModel> = emptyList(),
)