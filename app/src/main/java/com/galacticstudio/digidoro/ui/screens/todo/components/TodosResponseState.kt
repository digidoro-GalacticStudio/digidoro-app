package com.galacticstudio.digidoro.ui.screens.todo.components

/**
 * Sealed class representing different states of the login response using the API.
 */
sealed class TodosResponseState {

    object Resume : TodosResponseState()

    class Error(val exception: Exception) : TodosResponseState()

    data class ErrorWithMessage(val message: String) : TodosResponseState()

    object Success : TodosResponseState()
}
