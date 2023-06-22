package com.galacticstudio.digidoro.ui.screens.noteitem

/**
 * Sealed class representing different states of the note item response using the API.
 */
sealed class NoteItemResponseState {
    object Resume : NoteItemResponseState()
    class Error(val exception: Exception) : NoteItemResponseState()
    data class ErrorWithMessage(val message: String) : NoteItemResponseState()
    object Success : NoteItemResponseState()
}