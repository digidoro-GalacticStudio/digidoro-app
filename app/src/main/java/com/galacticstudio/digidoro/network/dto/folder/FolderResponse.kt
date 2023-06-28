package com.galacticstudio.digidoro.network.dto.folder

import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.google.gson.annotations.SerializedName

data class FolderListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<FolderData>,
    @SerializedName("totalCount") val totalCount: Int
)

data class FolderResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FolderData,
)

//The basic folder structure
data class FolderData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("theme") val theme: String,
    @SerializedName("notes_id") val notesId: List<String>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)

// All folder with notes
data class FolderNotesListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<FolderDataPopulated>,
    @SerializedName("totalCount") val totalCount: Int
)

data class FolderDataPopulated(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("theme") val theme: String,
    @SerializedName("notes_id") val notesId: List<NoteData>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)

// Get the selected folder
data class SelFolderResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SelectedFolderResponse
)

data class SelectedFolderResponse(
    @SerializedName("actualFolder") val actualFolder: FolderData?,
    @SerializedName("folders") val folders: List<FolderData>
)