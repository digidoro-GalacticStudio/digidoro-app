package com.galacticstudio.digidoro.network.dto.favoritenote

import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.google.gson.annotations.SerializedName

data class FavoriteNoteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any,
    @SerializedName("data") val data: List<FavoriteNote>,
    @SerializedName("totalCount") val totalCount: Int,
)

data class FavoriteNote(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("notes_id") val notes: List<NoteData>,
)

data class ToggleFavoriteNoteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any,
    @SerializedName("data") val data: List<ToggleFavoriteNote>,
)

data class SingleFavoriteNoteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any,
    @SerializedName("data") val data: ToggleFavoriteNote,
)

data class ToggleFavoriteNote(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("notes_id") val notesId: List<String>,
)