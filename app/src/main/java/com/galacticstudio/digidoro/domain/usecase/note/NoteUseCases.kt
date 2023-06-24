package com.galacticstudio.digidoro.domain.usecase.note

data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
)