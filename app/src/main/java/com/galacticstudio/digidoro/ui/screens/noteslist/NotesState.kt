package com.galacticstudio.digidoro.ui.screens.noteslist

import com.galacticstudio.digidoro.data.api.FolderModel
import com.galacticstudio.digidoro.data.api.FolderPopulatedModel
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType

data class NotesState (
    val notes: List<NoteModel> = emptyList(),
    val folders: List<FolderPopulatedModel> = emptyList(),
    val selectedFolder: FolderPopulatedModel? = null,
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isLoading: Boolean = false,
    val newFolderList: List<FolderModel> = emptyList(),
    val newSelectedFolder: FolderModel? = null, //The new folder
    val actualFolder: FolderModel? = null, //The previous folder
    val actualNoteId: String? = null,
)