package com.galacticstudio.digidoro.ui.screens.forgotpassword

sealed class ForgotPasswordFormEvent {
    data class EmailChanged(val email: String) : ForgotPasswordFormEvent()
    object Submit : ForgotPasswordFormEvent()
}