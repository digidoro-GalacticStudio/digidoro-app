package com.galacticstudio.digidoro.network.dto.note

data class NoteRequest(
    val title: String,
    val message: String,
    val tags: List<String>,
    val theme: String
)

data class NoteThemeRequest(
    val theme: String
)
