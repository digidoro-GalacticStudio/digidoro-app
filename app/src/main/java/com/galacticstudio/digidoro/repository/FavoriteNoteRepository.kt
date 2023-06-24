package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNote
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteRequest
import com.galacticstudio.digidoro.network.dto.favoritenote.ToggleFavoriteNote
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.service.FavoriteNoteService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class FavoriteNoteRepository(private val favoriteNoteService: FavoriteNoteService) {
    suspend fun getAllFavoriteNotes(
        populateFields: String? = null,
    ): ApiResponse<List<NoteData>> {
        return handleApiCall {
            favoriteNoteService.getAllFavoriteNotes(populateFields).data[0].notes
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