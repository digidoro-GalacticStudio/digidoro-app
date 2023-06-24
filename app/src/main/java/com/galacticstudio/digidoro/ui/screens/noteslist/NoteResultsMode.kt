package com.galacticstudio.digidoro.ui.screens.noteslist

sealed class NoteResultsMode {
    object AllNotes : NoteResultsMode()
    object FavoriteNotes : NoteResultsMode()
    object TrashNotes : NoteResultsMode()
    object FolderNotes : NoteResultsMode()
}