package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.folder.FolderData
import com.galacticstudio.digidoro.network.dto.folder.FolderDataPopulated
import com.galacticstudio.digidoro.network.dto.folder.FolderRequest
import com.galacticstudio.digidoro.network.dto.folder.FolderThemeRequest
import com.galacticstudio.digidoro.network.dto.folder.SelFolderResponse
import com.galacticstudio.digidoro.network.dto.folder.SelectedFolderResponse
import com.galacticstudio.digidoro.network.dto.folder.ToggleNoteRequest
import com.galacticstudio.digidoro.network.dto.folder.toFolderData
import com.galacticstudio.digidoro.network.dto.folder.toFolderModelEntity
import com.galacticstudio.digidoro.network.dto.folder.toPopulatedFolderData
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.service.FolderService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class FolderRepository(
    private val folderService: FolderService,
    private val database: DigidoroDataBase,
    private val context: Context
    ) {
    private val folderDao = database.FolderDao()

    suspend fun insertInDataBase(populateFields: String? = null): ApiResponse<String> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val responseApi = folderService.getAllFolders(populateFields)
                folderDao.insertAll(responseApi.toFolderModelEntity())
                "Inserted successfully"
            }
            else "could not insert into database"
            Log.d("folderNoteRepository", response)
            response
        }
    }
    suspend fun getAllFolders(populateFields: String? = null): ApiResponse<List<FolderDataPopulated>> {
        return handleApiCall {

            val response = if(CheckInternetConnectivity(context)){
                val responseApi = folderService.getAllFolders(populateFields)
                responseApi.data
            }
            else folderDao.getAllFolder().toPopulatedFolderData()


            response
        }
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
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val request = FolderRequest(name, theme, notesId)
                folderService.createFolder(request).data
            }
            else {
                val request = FolderModelEntity(name = name, theme = theme, notes_id = emptyList())
                folderDao.CreateFolder(request).toFolderData()
            }
            response
        }
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
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val request  = ToggleNoteRequest(noteId)
                folderService.toggleFolder(folderId, request).data
            }
            else folderDao.toggleNotesInFolder(folderId = folderId, noteId = noteId).toFolderData()

            response
        }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            folderDao.deleteFolders()

            "Deleted successfully"
        }
    }
}
