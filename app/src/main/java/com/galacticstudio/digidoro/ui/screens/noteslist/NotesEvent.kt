package com.galacticstudio.digidoro.ui.screens.noteslist

import com.galacticstudio.digidoro.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    object ToggleOrderSection: NotesEvent()
}