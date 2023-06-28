package com.galacticstudio.digidoro.ui.screens.edituser

data class EditUserFormState(
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
)

