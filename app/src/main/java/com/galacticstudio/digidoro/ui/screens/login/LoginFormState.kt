package com.galacticstudio.digidoro.ui.screens.login

/**
 * Represents the state of the login form.
 */
data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)
