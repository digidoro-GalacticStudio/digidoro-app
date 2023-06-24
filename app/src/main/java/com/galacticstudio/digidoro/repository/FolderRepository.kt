package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderData
import com.galacticstudio.digidoro.network.dto.folder.FolderRequest
import com.galacticstudio.digidoro.network.dto.folder.FolderThemeRequest
import com.galacticstudio.digidoro.network.service.FolderService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class FolderRepository(private val folderService: FolderService) {
    suspend fun getAllFolders(populateFields: String? = null): ApiResponse<List<FolderData>> {
        return handleApiCall { folderService.getAllFolders(populateFields).data }
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

    suspend fun toggleFolder(folderId: String): ApiResponse<FolderData> {
        return handleApiCall { folderService.toggleFolder(folderId).data }
    }
}
