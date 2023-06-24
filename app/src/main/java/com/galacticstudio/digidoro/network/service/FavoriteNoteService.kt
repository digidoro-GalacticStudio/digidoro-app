package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteRequest
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteResponse
import com.galacticstudio.digidoro.network.dto.favoritenote.SingleFavoriteNoteResponse
import com.galacticstudio.digidoro.network.dto.favoritenote.ToggleFavoriteNoteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteNoteService {
    @GET("api/favorite/own")
    suspend fun getAllFavoriteNotes(
        @Query("populateFields") populateFields: String? = null,
    ): FavoriteNoteResponse

    @GET("api/favorite/own")
    suspend fun getFavoriteNotes(): ToggleFavoriteNoteResponse

    @PATCH("api/favorite/own/toggleFav/{id}")
    suspend fun toggleFavoriteNote(
        @Path("id") favoriteNoteId: String,
        @Body note: FavoriteNoteRequest
    ): SingleFavoriteNoteResponse
}