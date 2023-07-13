package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
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
import com.google.gson.Gson

class NoteRepository(
    private val noteService: NoteService,
    private val database: DigidoroDataBase,
    private val context: Context
) {
    private val noteDao = database.NoteDao()
    suspend fun insertIntoDataBase(): ApiResponse<String> {
        return handleApiCall {

            val response = if(CheckInternetConnectivity(context)){
                val apiResponse = noteService.getAllNotes().data.toNoteModelEntity()
                Log.d("works", apiResponse.toString())
                //TODO: check works and notedatamoel
                noteDao.insertAll(apiResponse)
                "Inserted successfully"
            }
            else "could not insert into database"

            response
        }
    }

    suspend fun getAllNotes(
        sortBy: String = "createdAt",
        order: String = "desc",
        page: Int? = null,
        limit: Int? = null,
        populateFields: String? = null,
        isTrashed: Boolean = false,
    ): ApiResponse<List<NoteData>> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val apiResponse = noteService.getAllNotes(sortBy, order, page, limit, populateFields, isTrashed).data
                apiResponse
            } else noteDao.getAllNote(is_trash =  isTrashed, order = order.lowercase()).toListNoteData()

            response
        }
    }

    suspend fun getNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)) noteService.getNoteById(noteId).data
            else noteDao.getNote(noteId).toNoteData()

            response
        }
    }

    suspend fun createNote(
        title: String,
        message: String,
        tags: List<String>,
        theme: String
    ): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val request = NoteRequest(title, message, tags, theme)
                noteService.createNote(request).data
            }
            else{
                val request = NoteModelEntity(title = title, message = message, tags = tags, theme = theme)
                noteDao.createNote(request).toNoteData()
            }
            response
        }
    }

    suspend fun updateNoteById(
        noteId: String,
        title: String,
        message: String,
        tags: List<String>,
        theme: String
    ): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val request = NoteRequest(title, message, tags, theme)
                noteService.updateNoteById(noteId, request).data
            }
            else {
                val request = NoteModelEntity(title = title, message = message, tags =  tags, theme = theme)
                noteDao.updateNoteById(noteId, request).toNoteData()
            }

            response
        }
    }

    suspend fun deleteNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)) noteService.deleteNoteById(noteId).data
            else noteDao.deleteNoteById(noteId).toNoteData()

            response
        }
    }

    suspend fun updateThemeOfNoteById(noteId: String, theme: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val request = NoteThemeRequest(noteId)
                noteService.updateThemeOfNoteById(noteId, request).data
            }
            else {
                noteDao.toggleThemeById(id = noteId, theme = theme).toNoteData()
            }
            response
        }
    }

    suspend fun toggleTrashNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)) noteService.toggleTrashNoteById(noteId).data
            else noteDao.toggleTrashById(noteId).toNoteData()

            response
        }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            noteDao.deleteNotes()

            "Deleted successfully"
        }
    }
}
