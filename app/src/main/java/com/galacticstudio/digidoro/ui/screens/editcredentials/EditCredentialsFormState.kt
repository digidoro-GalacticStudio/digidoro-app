package com.galacticstudio.digidoro.ui.screens.editcredentials

data class EditCredentialsFormState(
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val email: String = "",
    val verificationCode: Int = -1,
)