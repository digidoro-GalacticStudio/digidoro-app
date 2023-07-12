package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.NoteRequest
import com.galacticstudio.digidoro.network.dto.note.NoteThemeRequest
import com.galacticstudio.digidoro.network.dto.note.toListNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteModelEntity
import com.galacticstudio.digidoro.network.service.NoteService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class NoteRepository(
    private val noteService: NoteService,
    private val database: DigidoroDataBase,
    private val context: Context
) {
    private val noteDao = database.NoteDao()
    suspend fun insertIntoDataBase(): ApiResponse<String> {
        return handleApiCall {

            val response = if(CheckInternetConnectivity(context)){
                val apiResponse = noteService.getAllNotes().data
                noteDao.insertAll(apiResponse.toNoteModelEntity())
                "Inserted successfully"
            }
            else "could not insert into database"

            response
        }
    }

    suspend fun getAllNotes(
        sortBy: String? = null,
        order: String? = null,
        page: Int? = null,
        limit: Int? = null,
        populateFields: String? = null,
        isTrashed: Boolean = false,
    ): ApiResponse<List<NoteData>> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val apiResponse = noteService.getAllNotes(sortBy, order, page, limit, populateFields, isTrashed).data
//                noteDao.insertAll(apiResponse.toNoteModelEntity())
                apiResponse
            } else noteDao.getAllNote(isTrashed).toListNoteData()

            response
        }
    }

    suspend fun getNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)) noteService.getNoteById(noteId).data
            else noteDao.getNoteById(noteId).toNoteData()

            response
        }
    }

    suspend fun createNote(
        title: String,
        message: String,
        tags: List<String>,
        theme: String
    ): ApiResponse<NoteData> {
        val request = NoteRequest(title, message, tags, theme)
        return handleApiCall { noteService.createNote(request).data }
    }

    suspend fun updateNoteById(
        noteId: String,
        title: String,
        message: String,
        tags: List<String>,
        theme: String
    ): ApiResponse<NoteData> {
        val request = NoteRequest(title, message, tags, theme)
        return handleApiCall { noteService.updateNoteById(noteId, request).data }
    }

    suspend fun deleteNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall { noteService.deleteNoteById(noteId).data }
    }

    suspend fun updateThemeOfNoteById(noteId: String, theme: String): ApiResponse<NoteData> {
        val request = NoteThemeRequest(noteId)
        return handleApiCall { noteService.updateThemeOfNoteById(noteId, request).data }
    }

    suspend fun toggleTrashNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall { noteService.toggleTrashNoteById(noteId).data }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            noteDao.deleteNotes()

            "Deleted successfully"
        }
    }
}
