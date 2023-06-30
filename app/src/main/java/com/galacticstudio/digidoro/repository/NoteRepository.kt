package com.galacticstudio.digidoro.repository

import android.util.Log
import com.galacticstudio.digidoro.data.db.dao.NoteDao
import com.galacticstudio.digidoro.data.db.room.NoteEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.NoteRequest
import com.galacticstudio.digidoro.network.dto.note.NoteThemeRequest
import com.galacticstudio.digidoro.network.service.NoteService
import com.galacticstudio.digidoro.repository.utils.handleApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NoteRepository(
    private val noteService: NoteService,
    private val noteDao: NoteDao
) {

    suspend fun syncNotes(notesFromApi: List<NoteData>) {
        Log.d("MyErrors", "notas: ${}")
        val apiResponse = getAllNotesFromApi()

        if (apiResponse is ApiResponse.Success) {
            val notes = apiResponse.data.map { noteData ->
                noteData.toNoteEntity()
            }
            notesFromApi.forEach { noteData ->
                val matchingNoteId = notes?.find { it._id == noteData.id }
                if (matchingNoteId == null) {
                    noteDao.insertNote(noteData.toNoteEntity())
                    Log.d("MyErrors", "INsert new note")
                }
            }

            Log.d("MyErrors", "Sync")


//            notes.forEach { note ->
//                noteDao.insertNote(note) // Inserta cada nota en la base de datos utilizando el DAO
//            }
        } else {
            Log.d("MyErrors", "Errors")
            // Handle API error
            // ...
        }
    }

    suspend fun syncDataFromApiToDatabase() {
        val apiResponse = getAllNotesFromApi()
        if (apiResponse is ApiResponse.Success) {
            val notes = apiResponse.data.map { noteData ->
                noteData.toNoteEntity()
            }
            notes.forEach { note ->
                noteDao.insertNote(note) // Inserta cada nota en la base de datos utilizando el DAO
            }
        } else {
            Log.d("MyErrors", "Errors")
            // Handle API error
            // ...
        }
    }

    suspend fun getAllNotesFromApi(
        sortBy: String? = null,
        order: String? = null,
        page: Int? = null,
        limit: Int? = null,
        populateFields: String? = null,
        isTrashed: Boolean? = null
    ): ApiResponse<List<NoteData>> {
        return handleApiCall {
            noteService.getAllNotes(
                sortBy, order, page, limit, populateFields, isTrashed
            ).data
        }
    }

    fun getAllNotesFromDatabase(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    suspend fun getNoteByIdFromApi(noteId: String): ApiResponse<NoteData> {
        return handleApiCall { noteService.getNoteById(noteId).data }
    }

    suspend fun getNoteByIdFromDatabase(noteId: String): NoteEntity? {
        return noteDao.getNoteById(noteId).firstOrNull()
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

    private fun NoteData.toNoteEntity(): NoteEntity {
        return NoteEntity(
            _id = this.id,
            user_id = this.userId,
            title = this.title,
            message = this.message,
            tags = this.tags,
            theme = this.theme,
            is_trashed = this.isTrashed,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
