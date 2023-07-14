package com.galacticstudio.digidoro.data.api

import java.util.Date

data class NoteModel(
    val id: String = "",
    val title: String,
    val message: String,
    val tags: List<String>,
    val theme: String = "#FFFFFF",
    val is_trashed: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
): EntityModel()
