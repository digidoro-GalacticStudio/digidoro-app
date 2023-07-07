package com.galacticstudio.digidoro.repository

import android.content.Context
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteRequest
import com.galacticstudio.digidoro.network.dto.favoritenote.ToggleFavoriteNote
import com.galacticstudio.digidoro.network.dto.favoritenote.toFavoriteNotesModelEntity
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteData
import com.galacticstudio.digidoro.network.service.FavoriteNoteService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class FavoriteNoteRepository(
    private val favoriteNoteService: FavoriteNoteService,
    private val database: DigidoroDataBase,
    private val context: Context
) {
    private val favoriteNoteDao = database.FavoriteNoteDao()
    suspend fun getAllFavoriteNotes(
        populateFields: String? = null,
    ): ApiResponse<List<NoteData>> {
        return handleApiCall {
            val reponse = if(CheckInternetConnectivity(context)){
                val response = favoriteNoteService.getAllFavoriteNotes(populateFields)

                favoriteNoteDao.insertAll(response.toFavoriteNotesModelEntity())

                response.data[0].notes
            }
            else favoriteNoteDao.getFavoriteNoteWithAllNotes()[0].notes_id.toNoteData()

            reponse
        }
    }

    suspend fun getFavoriteNote(): ApiResponse<ToggleFavoriteNote> {
        return handleApiCall { favoriteNoteService.getFavoriteNotes().data[0] }
    }

    suspend fun toggleFavoriteNote(
        favoriteNoteId: String,
        noteId: String,
    ): ApiResponse<List<String>> {
        val request = FavoriteNoteRequest(noteId)
        return handleApiCall {
            favoriteNoteService.toggleFavoriteNote(
                favoriteNoteId,
                request
            ).data.notesId
        }
    }
}
