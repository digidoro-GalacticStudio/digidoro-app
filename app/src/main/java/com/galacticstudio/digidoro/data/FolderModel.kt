package com.galacticstudio.digidoro.data

data class FolderModel(
    val id: String,
    val userId: String,
    val name: String,
    val theme: String,
    val notesId: List<String>,
    val createdAt: String,
    val updatedAt: String
)
