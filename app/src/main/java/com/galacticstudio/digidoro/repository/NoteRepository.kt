package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.NoteRequest
import com.galacticstudio.digidoro.network.dto.note.NoteThemeRequest
import com.galacticstudio.digidoro.network.dto.note.toListNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteData
import com.galacticstudio.digidoro.network.dto.note.toNoteModelData
import com.galacticstudio.digidoro.network.dto.note.toNoteModelEntity
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
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
    private val requestDao = database.PendingRequestDao()

    suspend fun insertIntoDataBase(): ApiResponse<String> {
        return handleApiCall {

            val response = if (CheckInternetConnectivity(context)) {
                val apiResponse = noteService.getAllNotes().data.toNoteModelEntity()
                //TODO: check works and notedatamoel
                noteDao.insertAll(apiResponse)
                "Inserted successfully"
            } else "could not insert into database"

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
//            val response = if (CheckInternetConnectivity(context)) {
//                val apiResponse = noteService.getAllNotes(
//                    sortBy,
//                    order,
//                    page,
//                    limit,
//                    populateFields,
//                    isTrashed
//                ).data
//                apiResponse
//            } else noteDao.getAllNote(is_trash = isTrashed, order = order.lowercase())
//                .toListNoteData()

            val response = noteDao.getAllNote(is_trash = isTrashed, order = order.lowercase())
                .toListNoteData()

            response
        }
    }

    suspend fun getNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
//            val response =
//                if (CheckInternetConnectivity(context)) noteService.getNoteById(noteId).data
//                else noteDao.getNote(noteId).toNoteData()
//
//            response
            noteDao.getNote(noteId).toNoteData()
        }
    }

    suspend fun createNote(
        title: String,
        message: String,
        tags: List<String>,
        theme: String,
        isTrashed: Boolean = false,
    ): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val request = NoteRequest(title, message, tags, theme)
                val apiResponse = noteService.createNote(request).data

                val requestRoom = NoteModelEntity(
                    _id = apiResponse.id,
                    title = apiResponse.title,
                    message = apiResponse.message,
                    tags = apiResponse.tags,
                    theme = apiResponse.theme
                )
                noteDao.createNote(requestRoom)

                if (isTrashed) noteService.toggleTrashNoteById(apiResponse.id)

                apiResponse
            } else {
                val request =
                    NoteModelEntity(title = title, message = message, tags = tags, theme = theme)
                val jsonString =
                    Gson().toJson(request.toNoteModelData()) // Convert the Note object to a JSON String
                // Create a pending request with the values of the new to-do object
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = Operation.CREATE,
                        data = jsonString,
                        entityModel = "NoteModel"
                    )
                )
                val roomResponse = noteDao.createNote(request).toNoteData()
                roomResponse
            }
            response
        }
    }

    suspend fun updateNoteById(
        noteId: String,
        title: String,
        message: String,
        tags: List<String>,
        theme: String,
        isTrashed: Boolean = false,
    ): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val request = NoteRequest(title, message, tags, theme)

                // Update the local database with the update
                val updatedNote =
                    NoteModelEntity(title = title, message = message, tags = tags, theme = theme)
                noteDao.updateNoteById(noteId, updatedNote).toNoteData()

                if (isTrashed) noteService.toggleTrashNoteById(noteId)

                noteService.updateNoteById(noteId, request).data
            } else {
                // Convert to json to be able to send it in my offline requests
                val jsonString = Gson().toJson(
                    NoteModel(
                        id = noteId,
                        title = title,
                        message = message,
                        tags = tags,
                        theme = theme
                    )
                )

                // Check if there is a previous request and update it
                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(noteId) }
                val operation =
                    if (existingRequest?.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                // Delete the existing request
                existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                // Insert the updated request
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = operation,
                        data = jsonString,
                        entityModel = "NoteModel"
                    )
                )

                val request =
                    NoteModelEntity(title = title, message = message, tags = tags, theme = theme)
                noteDao.updateNoteById(noteId, request).toNoteData()
            }

            response
        }
    }

    suspend fun deleteNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                noteDao.deleteNoteById(noteId)
                noteService.deleteNoteById(noteId).data
            } else {
                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(noteId) }

                existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                if (existingRequest?.operation == Operation.UPDATE || existingRequest == null) {
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.DELETE,
                            data = noteId,
                            entityModel = "NoteModel"
                        )
                    )
                }

                noteDao.deleteNoteById(noteId).toNoteData()
            }

            response
        }
    }

    suspend fun updateThemeOfNoteById(noteId: String, theme: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val request = NoteThemeRequest(noteId)
                noteService.updateThemeOfNoteById(noteId, request).data
            } else {
                noteDao.toggleThemeById(id = noteId, theme = theme).toNoteData()
            }
            response
        }
    }

    suspend fun toggleTrashNoteById(noteId: String): ApiResponse<NoteData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val apiResponse = noteService.toggleTrashNoteById(noteId).data

                // Update the local database with the toggle action
                noteDao.toggleTrashByStatus(id = noteId, apiResponse.isTrashed)
                apiResponse
            } else {

                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(noteId) }

                if (existingRequest == null || (existingRequest.operation == Operation.TOGGLE)) {
                    // Remove the previous request if it was a toggle operation
                    if (existingRequest?.operation == Operation.TOGGLE)
                        requestDao.deletePendingRequest(existingRequest._id)

                    // Insert a new pending request for toggling the NoteModel with the specified id
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.TOGGLE,
                            data = noteId,
                            entityModel = "NoteModel"
                        )
                    )
                } else {
                    requestDao.deletePendingRequest(existingRequest._id) //Remove the previous request

                    // Convert the existing request data from JSON to a NoteModel object
                    val converted = Gson().fromJson(existingRequest.data, NoteModel::class.java)

                    // Create a new NoteModel with the toggled state and convert the updated NoteModel back to JSON
                    val pendingEntity = NoteModel(
                        id = converted.id,
                        title = converted.title,
                        message = converted.message,
                        tags = converted.tags,
                        theme = converted.theme,
                        is_trashed = !converted.is_trashed
                    )

                    val jsonString = Gson().toJson(pendingEntity)
                    val operation =
                        if (existingRequest.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                    // Insert or update the latest request
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = operation,
                            data = jsonString,
                            entityModel = "NoteModel"
                        )
                    )
                }

                // Update the local database with the toggle action
                noteDao.toggleTrashById(noteId).toNoteData()
            }

            response
        }
    }

    suspend fun deleteNoteLocalDatabase(id: String): ApiResponse<Unit> {
        return handleApiCall {
            noteDao.deleteNoteById(id).toNoteData()
        }
    }

    suspend fun deleteAll(): ApiResponse<String> {
        return handleApiCall {
            noteDao.deleteNotes()

            "Deleted successfully"
        }
    }
}
