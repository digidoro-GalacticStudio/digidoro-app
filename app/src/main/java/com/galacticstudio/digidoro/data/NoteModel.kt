package com.galacticstudio.digidoro.data

import java.util.Date

//TODO Verify the attributes (user_id)
data class NoteModel(
    val user_id: Int,
    val title: String,
    val message: String,
    val tags: List<String>,
    val theme: String = "#FFFFFF",
    val is_trashed: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
