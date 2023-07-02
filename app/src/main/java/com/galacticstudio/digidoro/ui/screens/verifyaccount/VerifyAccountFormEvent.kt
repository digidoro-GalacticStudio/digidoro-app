package com.galacticstudio.digidoro.ui.screens.verifyaccount

sealed class VerifyAccountFormEvent {
    data class CodeChanged(val code: String) : VerifyAccountFormEvent()
    data class EmailChanged(val email: String) : VerifyAccountFormEvent()
    object Submit : VerifyAccountFormEvent()
    object ResendCode : VerifyAccountFormEvent()
}