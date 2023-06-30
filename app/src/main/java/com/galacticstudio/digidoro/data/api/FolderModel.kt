package com.galacticstudio.digidoro.data.api

data class FolderModel(
    val id: String,
    val userId: String,
    val name: String,
    val theme: String,
    val notesId: List<String>,
    val createdAt: String,
    val updatedAt: String
)

data class FolderPopulatedModel(
    val id: String,
    val userId: String,
    val name: String,
    val theme: String,
    val notesId: List<NoteModel>,
    val createdAt: String,
    val updatedAt: String
)

fun convertToFolderPopulatedModel(folder: FolderModel): FolderPopulatedModel {
    val emptyNotes = folder.notesId.map { NoteModel(it, "", "", "", listOf(), "", false) }
    return FolderPopulatedModel(
        folder.id,
        folder.userId,
        folder.name,
        folder.theme,
        emptyNotes,
        folder.createdAt,
        folder.updatedAt
    )
}
