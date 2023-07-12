package com.galacticstudio.digidoro.network.dto.note

import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.network.dto.folder.FolderNotesListResponse
import com.google.gson.annotations.SerializedName

data class NoteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: NoteData
)

data class NoteListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<NoteData>
)

data class NoteData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("theme") val theme: String,
    @SerializedName("is_trashed") val isTrashed: Boolean,
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)

fun List<NoteData>.toNoteModelEntity(): List<NoteModelEntity>{
    return map{ element ->
        NoteModelEntity(
            _id = element.id,
            user_id = element.userId,
            title = element.title,
            message = element.message,
            tags = element.tags,
            theme = element.theme,
            is_trashed = element.isTrashed,
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }.toList()
}

fun List<NoteModelEntity>.toListNoteData(): List<NoteData>{
    return map{ element ->
        NoteData(
            id = element._id,
            userId = element.user_id,
            title = element.title,
            message = element.message,
            tags = element.tags,
            theme = element.theme,
            isTrashed = element.is_trashed,
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }.toList()
}

fun NoteModelEntity.toNoteData(): NoteData{
    return NoteData(
        id = this._id,
        userId = this.user_id,
        title = this.title,
        message = this.message,
        tags = this.tags,
        theme = this.theme,
        isTrashed = this.is_trashed,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}