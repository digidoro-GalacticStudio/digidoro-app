package com.galacticstudio.digidoro.network.dto.folder

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

data class FolderData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("theme") val theme: String,
    @SerializedName("notes_id") val notesId: List<String>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)