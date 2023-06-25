package com.galacticstudio.digidoro.ui.screens.noteslist

import com.galacticstudio.digidoro.data.FolderModel
import com.galacticstudio.digidoro.data.FolderPopulatedModel
import com.galacticstudio.digidoro.data.NoteModel
import com.galacticstudio.digidoro.domain.util.NoteOrder
import com.galacticstudio.digidoro.domain.util.OrderType

data class NotesState (
    val notes: List<NoteModel> = emptyList(),
    val folders: List<FolderPopulatedModel> = emptyList(),
    val selectedFolder: FolderPopulatedModel? = null,
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isLoading: Boolean = false,
)