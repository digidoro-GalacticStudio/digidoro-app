package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.folder.FolderListResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderRequest
import com.galacticstudio.digidoro.network.dto.folder.FolderResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderThemeRequest
import com.galacticstudio.digidoro.network.dto.note.NoteThemeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FolderService {
    @GET("api/folder/own")
    suspend fun getAllFolders(
        @Query("populateFields") populateFields: String? = null,
    ): FolderListResponse

    @GET("api/folder/own/{id}")
    suspend fun getFolderById(
        @Path("id") folderId: String,
        @Query("populateFields") populateFields: String? = null,
    ): FolderResponse

    @POST("api/folder/own")
    suspend fun createFolder(@Body note: FolderRequest): FolderResponse

    @PATCH("api/folder/own/{id}")
    suspend fun updateNoteById(@Path("id") folderId: String, @Body folder: FolderRequest): FolderResponse

    @DELETE("api/folder/own/{id}")
    suspend fun deleteNoteById(@Path("id") folderId: String): FolderResponse

    //TODO :: Experimental
    @PATCH("api/folder/own/theme/{id}")
    suspend fun updateThemeFolderById(@Path("id") folderId: String, @Body theme: FolderThemeRequest): FolderResponse

    @PATCH("api/folder/own/toggleItems/{id}")
    suspend fun toggleFolder(@Path("id") folderId: String): FolderResponse
}