package com.galacticstudio.digidoro.ui.screens.editcredentials

sealed class EditCredentialsResponseState {
    object Resume : EditCredentialsResponseState()
    class Error(val exception: Exception) : EditCredentialsResponseState()
    data class ErrorWithMessage(val message: String) : EditCredentialsResponseState()
    object Success : EditCredentialsResponseState()
}

