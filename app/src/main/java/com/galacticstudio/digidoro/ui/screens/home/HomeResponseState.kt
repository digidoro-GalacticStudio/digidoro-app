package com.galacticstudio.digidoro.ui.screens.home

sealed class HomeResponseState {
    object Resume : HomeResponseState()
    class Error(val exception: Exception) : HomeResponseState()
    data class ErrorWithMessage(val message: String) : HomeResponseState()
    object Success : HomeResponseState()
}