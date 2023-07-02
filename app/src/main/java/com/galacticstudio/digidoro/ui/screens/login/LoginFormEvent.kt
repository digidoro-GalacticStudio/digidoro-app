package com.galacticstudio.digidoro.ui.screens.login

/**
 * Sealed class representing different events in the login form.
 */
sealed class LoginFormEvent {
    // Event representing a change in the email field of the login form.
    data class EmailChanged(val email: String) : LoginFormEvent()
    // Event representing a change in the password field of the login form.
    data class PasswordChanged(val password: String) : LoginFormEvent()
    // Event representing the submission of the login form.
    object Submit: LoginFormEvent()
}