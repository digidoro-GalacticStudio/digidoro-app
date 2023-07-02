package com.galacticstudio.digidoro.ui.screens.verifyaccount

sealed class VerifyAccountResponseState {
    object Resume : VerifyAccountResponseState()
    class Error(val exception: Exception) : VerifyAccountResponseState()
    data class ErrorWithMessage(val message: String) : VerifyAccountResponseState()
    data class Success(val complete: Boolean) : VerifyAccountResponseState()
}
