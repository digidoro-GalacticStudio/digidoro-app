package com.galacticstudio.digidoro.ui.screens.register

/**
 * Represents the state of the register form.
 */
data class RegisterFormState(
    val firstname: String = "",
    val firstnameError: String? = null,
    val lastname: String = "",
    val lastnameError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val birthday: String = "",
    val birthdayError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
)
