package com.galacticstudio.digidoro.ui.screens.editcredentials

sealed class EditCredentialsFormEvent {
    data class PasswordChanged(val password: String) : EditCredentialsFormEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : EditCredentialsFormEvent()
    data class VerificationCodeChanged(val code: Int) : EditCredentialsFormEvent()
    data class EmailChanged(val email: String) : EditCredentialsFormEvent()
    object Submit : EditCredentialsFormEvent()
}