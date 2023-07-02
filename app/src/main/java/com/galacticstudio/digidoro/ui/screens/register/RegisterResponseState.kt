package com.galacticstudio.digidoro.ui.screens.register

/**
 * Sealed class representing different states of the login response using the API.
 */
sealed class RegisterResponseState {
    object Resume : RegisterResponseState()
    class Error(val exception: Exception) : RegisterResponseState()
    data class ErrorWithMessage(val message: String) : RegisterResponseState()
    object Success : RegisterResponseState()
}