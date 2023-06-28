package com.galacticstudio.digidoro.ui.screens.home

import com.galacticstudio.digidoro.data.UserRankingModel

data class HomeUIState(
    val username: String = "",
    val user: UserRankingModel? = null,
    //TODO Get the notes and the pomodoro sesions
)