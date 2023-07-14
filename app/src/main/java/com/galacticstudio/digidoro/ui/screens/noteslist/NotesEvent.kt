package com.galacticstudio.digidoro.ui.screens.noteslist

import com.galacticstudio.digidoro.data.api.FolderModel
import com.galacticstudio.digidoro.data.api.FolderPopulatedModel
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.domain.util.NoteOrder

sealed class NotesEvent {
    data class ResultsChanged(val resultsMode: NoteResultsMode): NotesEvent()
    data class SelectedFolderChanged(val folder: FolderPopulatedModel): NotesEvent()
    data class LoadingChanged(val isLoading: Boolean): NotesEvent()
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class RolesChanged(val roles: List<String>): NotesEvent()
    data class Rebuild(val resultsMode: NoteResultsMode): NotesEvent()
    object ClearData: NotesEvent()
    data class DuplicateNote(val note: NoteModel?): NotesEvent()
    data class DeleteNote(val noteId: String): NotesEvent()
    data class ToggleTrash(val noteId: String): NotesEvent()
    data class GetSelectedFolder(val noteId: String): NotesEvent()
    data class NewFolderNoteChanged(val folder: FolderModel?): NotesEvent()
    object MoveToAnotherFolder: NotesEvent()
}