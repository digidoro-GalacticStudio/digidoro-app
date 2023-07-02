package com.galacticstudio.digidoro.ui.screens.edituser

sealed class EditUserResponseState {
    object Resume : EditUserResponseState()
    class Error(val exception: Exception) : EditUserResponseState()
    data class ErrorWithMessage(val message: String) : EditUserResponseState()
    object Success : EditUserResponseState()
}