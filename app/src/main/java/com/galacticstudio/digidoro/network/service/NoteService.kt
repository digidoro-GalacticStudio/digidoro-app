package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.note.NoteListResponse
import com.galacticstudio.digidoro.network.dto.note.NoteRequest
import com.galacticstudio.digidoro.network.dto.note.NoteResponse
import com.galacticstudio.digidoro.network.dto.note.NoteThemeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NoteService {
    @GET("api/note/own")
    suspend fun getAllNotes(
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("populateFields") populateFields: String? = null,
    ): NoteListResponse

    @GET("api/notes/own/{id}")
    suspend fun getNoteById(@Path("id") noteId: String): NoteResponse

    @POST("api/note/own")
    suspend fun createNote(@Body note: NoteRequest): NoteResponse

    @PUT("api/notes/own/{id}")
    suspend fun updateNoteById(@Path("id") noteId: String, @Body note: NoteRequest): NoteResponse

    @DELETE("api/notes/own/{id}")
    suspend fun deleteNoteById(@Path("id") noteId: String): NoteResponse

    //TODO :: Experimental
    @PUT("api/notes/own/theme/{id}")
    suspend fun updateThemeOfNoteById(@Path("id") noteId: String, @Body theme: NoteThemeRequest): NoteResponse

    @PUT("api/notes/own/theme/{id}")
    suspend fun toggleTrashNoteById(@Path("id") noteId: String): NoteResponse
}