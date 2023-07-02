package com.galacticstudio.digidoro.ui.screens.register

import java.util.Date

/**
 * Sealed class representing different events in the register form.
 */
sealed class RegisterFormEvent {
    // Event representing a change in the email field of the register form.
    data class EmailChanged(val email: String) : RegisterFormEvent()

    // Event representing a change in the password field of the register form.
    data class PasswordChanged(val password: String) : RegisterFormEvent()

    // Event representing a change in the firstname field of the register form.
    data class FirstnameChanged(val firstname: String) : RegisterFormEvent()

    // Event representing a change in the lastname field of the register form.
    data class LastnameChanged(val lastname: String) : RegisterFormEvent()

    // Event representing a change in the username field of the register form.
    data class UsernameChanged(val username: String) : RegisterFormEvent()

    // Event representing a change in the username field of the register form.
    data class PhoneNumberChanged(val phone_number: String) : RegisterFormEvent()

    // Event representing a change in the birthdate field of the register form.
    data class BirthdayChanged(val birthdate: String) : RegisterFormEvent()

    // Event representing a change in the confirmPassword field of the register form.
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterFormEvent()

    // Event representing the submission of the register form.
    object Submit : RegisterFormEvent()
}
