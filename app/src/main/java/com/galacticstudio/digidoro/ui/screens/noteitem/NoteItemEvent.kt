package com.galacticstudio.digidoro.ui.screens.noteitem

sealed class NoteItemEvent {
    data class NoteIdChanged(val noteId: String): NoteItemEvent()
    data class FolderIdChanged(val folderId: String): NoteItemEvent()
    data class TitleChanged(val title: String): NoteItemEvent()
    data class ContentChanged(val message: String): NoteItemEvent()
    data class ColorChanged(val color: String): NoteItemEvent()
    data class TagsChanged(val tags: List<String>): NoteItemEvent()
    data class ActionTypeChanged(val actionType: ActionType): NoteItemEvent()
    object SaveNote: NoteItemEvent()
    object UpdateNote: NoteItemEvent()
    object DeleteNote: NoteItemEvent()
    object ToggleFavorite: NoteItemEvent()
    object ToggleTrash: NoteItemEvent()
}