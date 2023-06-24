package com.galacticstudio.digidoro.network.dto.folder

data class FolderRequest(
    val name: String,
    val theme: String,
    val notes_id: List<String> = emptyList(),
)

data class FolderThemeRequest(
    val theme: String
)
