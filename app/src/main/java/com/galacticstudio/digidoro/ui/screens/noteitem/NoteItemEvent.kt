package com.galacticstudio.digidoro.ui.screens.noteitem

sealed class NoteItemEvent {
    data class TitleChanged(val title: String): NoteItemEvent()
    data class ContentChanged(val message: String): NoteItemEvent()
    data class ColorChanged(val color: Int): NoteItemEvent()
    data class TagsChanged(val tags: List<String>): NoteItemEvent()
    data class DeleteNote(val noteId: String): NoteItemEvent()
    object SaveNote: NoteItemEvent()
    object ToggleFavorite: NoteItemEvent()
}