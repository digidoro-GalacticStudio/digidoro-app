package com.galacticstudio.digidoro.network.dto.favoritenote

import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.todo.ResponseAllTodo
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

//Responses for FavoriteNoteDao Room DataBase
data class FavoriteNoteDao(
    val _id: String,
    val user_id: String,
    val notes_id: List<String>
)

fun FavoriteNoteResponse.toFavoriteNotesModelEntity(): MutableList<FavoriteNotesModelEntity>{
    val response = data.mapIndexed{ _, element ->
        val listId = element.notes.map { it.id }
        FavoriteNotesModelEntity(
            _id = element.id,
            user_id = element.userId,
            notes_id = listId
        )
    }

    return response.toMutableList()
}