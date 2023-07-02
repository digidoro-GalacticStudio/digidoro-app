package com.galacticstudio.digidoro.ui.screens.login

/**
 * Sealed class representing different states of the login response using the API.
 */
sealed class LoginResponseState {
    object Resume : LoginResponseState()
    class Error(val exception: Exception) : LoginResponseState()
    data class ErrorWithMessage(val message: String) : LoginResponseState()
    data class Success(
        val token: String,
        val username: String,
        val roles: List<String> = emptyList(),
        val _id: String
    ) : LoginResponseState()
}