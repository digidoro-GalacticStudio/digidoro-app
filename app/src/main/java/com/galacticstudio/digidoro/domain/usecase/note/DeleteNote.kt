package com.galacticstudio.digidoro.domain.usecase.note

import com.galacticstudio.digidoro.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: String) {
        repository.deleteNoteById(noteId)
    }
}