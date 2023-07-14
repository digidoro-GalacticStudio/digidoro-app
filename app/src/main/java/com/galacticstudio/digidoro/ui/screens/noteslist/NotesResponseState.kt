package com.galacticstudio.digidoro.ui.screens.noteslist

/**
 * Sealed class representing different states of the note list response using the API.
 */
sealed class NotesResponseState {
    object Resume : NotesResponseState()
    class Error(val exception: Exception) : NotesResponseState()
    data class ErrorWithMessage(val message: String) : NotesResponseState()
    data class Success(val reload: Boolean) : NotesResponseState()
}