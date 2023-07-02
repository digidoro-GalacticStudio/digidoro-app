package com.galacticstudio.digidoro.ui.screens.verifyaccount

data class VerifyAccountFormState(
    val email: String = "",
    val code: String = "",
    val codeError: String? = null,
)