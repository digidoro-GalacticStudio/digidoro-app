package com.galacticstudio.digidoro.ui.screens.home

sealed class HomeUIEvent {
    data class UsernameChanged(val username: String) : HomeUIEvent()
}