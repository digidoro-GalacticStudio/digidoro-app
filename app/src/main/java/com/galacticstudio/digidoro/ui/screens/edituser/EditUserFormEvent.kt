package com.galacticstudio.digidoro.ui.screens.edituser

sealed class EditUserFormEvent {
    data class FirstnameChanged(val firstname: String) : EditUserFormEvent()
    data class LastnameChanged(val lastname: String) : EditUserFormEvent()
    data class UsernameChanged(val username: String) : EditUserFormEvent()
    data class PhoneNumberChanged(val phone_number: String) : EditUserFormEvent()
    data class BirthdayChanged(val birthdate: String) : EditUserFormEvent()
    object Submit : EditUserFormEvent()
    object Rebuild : EditUserFormEvent()
}