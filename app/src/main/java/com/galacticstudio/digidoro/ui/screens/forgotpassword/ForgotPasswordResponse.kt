package com.galacticstudio.digidoro.ui.screens.forgotpassword

sealed class ForgotPasswordResponse {
    object Resume : ForgotPasswordResponse()
    class Error(val exception: Exception) : ForgotPasswordResponse()
    data class ErrorWithMessage(val message: String) : ForgotPasswordResponse()
    object Success : ForgotPasswordResponse()
}