package com.galacticstudio.digidoro.network.dto.folder

import com.galacticstudio.digidoro.data.db.dao.FolderDao
import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.toListNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteModelEntity
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

fun  FolderNotesListResponse.toFolderModelEntity(): List<FolderModelEntity>{
    val response = data.mapIndexed{ _, element ->
        FolderModelEntity(
            _id = element.id,
            user_id = element.userId,
            name = element.name,
            theme = element.theme,
            notes_id = element.notesId.toNoteModelEntity(),
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }

    return response.toList()
}

fun  List<FolderModelEntity>.toPopulatedFolderData(): List<FolderDataPopulated>{
    return map{ element ->
        FolderDataPopulated(
            id = element._id,
            userId = element.user_id,
            name = element.name,
            theme = element.theme,
            notesId = element.notes_id.toListNoteData(),
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }
}

fun  List<FolderModelEntity>.toListFolderData(): List<FolderData>{
    return map{ element ->
        FolderData(
            id = element._id,
            userId = element.user_id,
            name = element.name,
            theme = element.theme,
            notesId = element.notes_id.map { it._id },
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }
}
fun  FolderModelEntity.toFolderData(): FolderData{
    val arrayId = this.notes_id.map { element -> element._id }
    return FolderData(
            id = this._id,
            userId = this.user_id,
            name = this.name,
            theme = this.theme,
            notesId = arrayId,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
}
