package com.galacticstudio.digidoro.ui.screens.noteslist

import com.galacticstudio.digidoro.data.FolderPopulatedModel
import com.galacticstudio.digidoro.domain.util.NoteOrder

sealed class NotesEvent {
    data class ResultsChanged(val resultsMode: NoteResultsMode): NotesEvent()
    data class SelectedFolderChanged(val folder: FolderPopulatedModel): NotesEvent()
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class RolesChanged(val roles: List<String>): NotesEvent()
    object ToggleOrderSection: NotesEvent()
    object Rebuild: NotesEvent()
}