package com.galacticstudio.digidoro.ui.screens.noteitem

data class NoteItemState(
    val noteId: String = "",
    val folderId: String = "",
    val favoriteContainerId: String = "",
    val title: String = "",
    val message: String = "",
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val noteError: String? = "",
)
