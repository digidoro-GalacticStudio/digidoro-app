package com.galacticstudio.digidoro.repository

import android.util.Log
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderData
import com.galacticstudio.digidoro.network.dto.folder.FolderDataPopulated
import com.galacticstudio.digidoro.network.dto.folder.FolderRequest
import com.galacticstudio.digidoro.network.dto.folder.FolderThemeRequest
import com.galacticstudio.digidoro.network.dto.folder.SelFolderResponse
import com.galacticstudio.digidoro.network.dto.folder.SelectedFolderResponse
import com.galacticstudio.digidoro.network.dto.folder.ToggleNoteRequest
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.service.FolderService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class FolderRepository(
    private val folderService: FolderService,
    private val database: DigidoroDataBase
    ) {
    private val folderDao = database.FolderDao()
    suspend fun getAllFolders(populateFields: String? = null): ApiResponse<List<FolderDataPopulated>> {
        return handleApiCall { folderService.getAllFolders(populateFields).data }
    }

    suspend fun getAllWithoutFolders(): ApiResponse<List<NoteData>> {
        return handleApiCall { folderService.getAllWithoutFolders().data }
    }

    suspend fun getSelectedFolders(folderId: String): ApiResponse<SelectedFolderResponse> {
        return handleApiCall { folderService.getSelectedFolders(folderId).data }
    }

    suspend fun getFolderById(folderId: String, populateFields: String? = null): ApiResponse<FolderData> {
        return handleApiCall { folderService.getFolderById(folderId, populateFields).data }
    }

    suspend fun createFolder(name: String, theme: String, notesId: List<String> = emptyList()): ApiResponse<FolderData> {
        val request = FolderRequest(name, theme, notesId)
        return handleApiCall { folderService.createFolder(request).data }
    }

    suspend fun updateFolderById(
        folderId: String,
        name: String,
        theme: String,
        notesId: List<String>
    ): ApiResponse<FolderData> {
        val request = FolderRequest(name, theme, notesId)
        return handleApiCall { folderService.updateNoteById(folderId, request).data }
    }

    suspend fun deleteFolderById(folderId: String): ApiResponse<FolderData> {
        return handleApiCall { folderService.deleteNoteById(folderId).data }
    }

    suspend fun updateThemeOfFolderById(folderId: String, theme: String): ApiResponse<FolderData> {
        val request = FolderThemeRequest(theme)
        return handleApiCall { folderService.updateThemeFolderById(folderId, request).data }
    }

    suspend fun toggleFolder(folderId: String, noteId: String): ApiResponse<FolderData> {
        val request  = ToggleNoteRequest(noteId)
        Log.d("MyErrors", "MY REQUEST: ${request}")
        return handleApiCall { folderService.toggleFolder(folderId, request).data }
    }
}
