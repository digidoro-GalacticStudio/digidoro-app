package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import com.galacticstudio.digidoro.data.api.PomodoroModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.data.db.models.PomodoroModelEntity
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.toNoteData
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroData
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroRequest
import com.galacticstudio.digidoro.network.dto.pomodoro.toListPomdoroModelEntity
import com.galacticstudio.digidoro.network.dto.pomodoro.toListPomodoroData
import com.galacticstudio.digidoro.network.dto.pomodoro.toPomodoroData
import com.galacticstudio.digidoro.network.dto.pomodoro.toPomodoroModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
import com.galacticstudio.digidoro.network.service.PomodoroService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall
import com.galacticstudio.digidoro.util.DateUtils
import com.google.gson.Gson

class PomodoroRepository(
    private val pomodoroService: PomodoroService,
    private val database: DigidoroDataBase,
    private val context: Context
) {
    private val pomodoroDao = database.PomodoroDao()
    private val requestDao = database.PendingRequestDao()

    suspend fun insertIntoDataBase(): ApiResponse<String> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val apiResponse = pomodoroService.getAllPomodoros().data
                pomodoroDao.insertAll(apiResponse.toListPomdoroModelEntity())
                "Inserted successfully"
            } else "could not insert into database"
            response
        }
    }

    suspend fun getAllPomodoros(
        sortBy: String? = null,
        order: String? = null,
        page: Int? = null,
        limit: Int? = null,
        populate: String? = null,
    ): ApiResponse<List<PomodoroData>> {
        return handleApiCall {
//            val response = if (CheckInternetConnectivity(context)) {
//                val apiResponse = pomodoroService.getAllPomodoros(
//                    sortBy,
//                    order,
//                    page,
//                    limit,
//                    populate
//                ).data
//                apiResponse
//            } else pomodoroDao.getAllPomodoros().toListPomodoroData()
            val response = pomodoroDao.getAllPomodoros().toListPomodoroData()

            response
        }
    }

    suspend fun getPomodoroById(pomodoroId: String): ApiResponse<PomodoroData> {
        return handleApiCall {
            val response =
                if (CheckInternetConnectivity(context)) pomodoroService.getPomodoroById(pomodoroId).data
                else pomodoroDao.getPomodoroById(pomodoroId).toPomodoroData()

            response
        }
    }

    suspend fun createPomodoro(
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ): ApiResponse<PomodoroData> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context)) {
                val request = PomodoroRequest(name, sessionsCompleted, totalSessions, theme)
                val apiResponse = pomodoroService.createPomodoro(request).data
                Log.d("MyErrors", " ++ ap res ++ : " + apiResponse)

                //Update the room database with the new To-do
                val requestRoom = PomodoroModelEntity(
                    _id = apiResponse.id,
                    name = apiResponse.name,
                    sessions_completed = apiResponse.sessionsCompleted,
                    total_sessions = apiResponse.totalSessions,
                    theme = apiResponse.theme,
                )
                pomodoroDao.insertPomodoro(requestRoom)

                apiResponse
            } else {
                val request = PomodoroModelEntity(
                    name = name,
                    sessions_completed = sessionsCompleted,
                    total_sessions = totalSessions,
                    theme = theme
                )

                val jsonString =
                    Gson().toJson(request.toPomodoroModel()) // Convert the to-do object to a JSON String
                // Create a pending request with the values of the new to-do object
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = Operation.CREATE,
                        data = jsonString,
                        entityModel = "PomodoroModel"
                    )
                )

                pomodoroDao.insertPomodoro(request).toPomodoroData()
            }

            response
        }
    }

    suspend fun updatePomodoro(
        pomodoroId: String,
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ): ApiResponse<PomodoroData> {
        return handleApiCall {

            val response = if (CheckInternetConnectivity(context)) {
                val request = PomodoroRequest(name, sessionsCompleted, totalSessions, theme)

                // Update the local database with the update
                val updatedPomodoro = PomodoroModelEntity(
                    name = name,
                    sessions_completed = sessionsCompleted,
                    total_sessions = totalSessions,
                    theme = theme
                )
                pomodoroDao.updatePomodoroById(pomodoroId, updatedPomodoro).toPomodoroData()

                pomodoroService.updatePomodoro(pomodoroId, request).data
            } else {
                // Convert to json to be able to send it in my offline requests
                val jsonString = Gson().toJson(
                    PomodoroModel(
                        id = pomodoroId,
                        name = name,
                        sessionsCompleted = sessionsCompleted,
                        totalSessions = totalSessions,
                        theme = theme
                    )
                )

                // Check if there is a previous request and update it
                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(pomodoroId) }
                val operation =
                    if (existingRequest?.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                // Delete the existing request
                existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                // Insert the updated request
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = operation,
                        data = jsonString,
                        entityModel = "PomodoroModel"
                    )
                )

                val request = PomodoroModelEntity(
                    name = name,
                    sessions_completed = sessionsCompleted,
                    total_sessions = totalSessions,
                    theme = theme
                )
                pomodoroDao.updatePomodoroById(pomodoroId, request).toPomodoroData()
            }
            response
        }
    }

    suspend fun incrementSessions(
        pomodoroId: String,
    ): ApiResponse<String> {
        return handleApiCall {
            val response =
                if (CheckInternetConnectivity(context)) pomodoroService.incrementSessions(pomodoroId).message
                else pomodoroDao.updateCompletedSessionsById(pomodoroId)

            response
        }
    }

    suspend fun deletePomodoro(pomodoroId: String): ApiResponse<PomodoroData> {
        return handleApiCall {
            val response =
                if (CheckInternetConnectivity(context)) {
                    val response = pomodoroService.deletePomodoro(pomodoroId).data
                    // Update the local database with the update
                    pomodoroDao.deletePomodoroById(pomodoroId)
                    response
                } else {
                    val existingRequest =
                        requestDao.getAllPendingRequests().find { it.data.contains(pomodoroId) }

                    existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                    if (existingRequest?.operation == Operation.UPDATE || existingRequest == null) {
                        requestDao.insertPendingRequest(
                            PendingRequestEntity(
                                operation = Operation.DELETE,
                                data = pomodoroId,
                                entityModel = "PomodoroModel"
                            )
                        )
                    }

                    pomodoroDao.deletePomodoroById(pomodoroId).toPomodoroData()
                }

            response
        }
    }

    suspend fun deletePomodoroLocalDatabase(id: String): ApiResponse<Unit> {
        return handleApiCall {
            pomodoroDao.deletePomodoroById(id)
        }
    }

    suspend fun deleteAll(): ApiResponse<String> {
        return handleApiCall {
            pomodoroDao.deletePomodoros()

            "Deleted successfully"
        }
    }
}
