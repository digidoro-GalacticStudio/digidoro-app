package com.galacticstudio.digidoro.work

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.api.EntityModel
import com.galacticstudio.digidoro.data.api.NoteModel
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.repository.RequestRepository
import com.galacticstudio.digidoro.repository.TodoRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class SynchronizationWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    private lateinit var pendingRequestRepository: RequestRepository
    private lateinit var todoRepository: TodoRepository
    private lateinit var noteRepository: NoteRepository
    private val thisContext = context

    override suspend fun doWork(): Result  {
        val app = applicationContext as Application
        todoRepository = app.todoRepository
        noteRepository = app.notesRepository
        pendingRequestRepository = app.pendingRequestRepository

        val pendingRequests = getPendingRequestsFromDatabase()

        if (pendingRequests.isNotEmpty()) {
            // Show a notification
            startForegroundService()
            // Indicates that the synchronization task has started
            syncStatusLiveData.postValue(SyncStatus.IN_PROGRESS)
        } else return Result.success()

        Log.d("MyErrors", "Do Work [" +pendingRequests.size + "] : "+ pendingRequests)

        for (request in pendingRequests) {
            val success = syncWithApi(request)

            if (success) {
                deletePendingRequest(request)
            } else {
                deletePendingRequest(request)
                // Handle the error
            }
        }

        // Indicates that the synchronization task has finished
        syncStatusLiveData.postValue(SyncStatus.COMPLETED)

        return Result.success()
    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(thisContext, "synchronization_channel")
                    .setSmallIcon(R.drawable.igi_logo)
                    .setContentText("Synchronization...")
                    .setContentTitle("sync in progress")
                    .build()
            )
        )
    }

    private fun getPendingRequestsFromDatabase(): List<PendingRequestEntity> {
        return runBlocking {
            when (val response = pendingRequestRepository.getAllRequest()) {
                is ApiResponse.Success -> {
                    response.data
                }

                else -> {
                    emptyList()
                }
            }
        }
    }

    private suspend fun syncWithApi(request: PendingRequestEntity): Boolean {
        val gson = Gson()
        val operation = request.operation
        val jsonData = request.data
        val entityType = request.entityModel

        return try {
            when (operation) {
                Operation.CREATE -> {
                    val entity: EntityModel? = when (entityType) {
                        "TodoModel" -> gson.fromJson(jsonData, TodoModel::class.java)
                        "NoteModel" -> gson.fromJson(jsonData, NoteModel::class.java)
                        else -> null
                    }

                    val success = entity?.let { handleCreateOperation(it) }
                    success ?: false
                }

                Operation.UPDATE -> {
                    val entity: EntityModel? = when (entityType) {
                        "TodoModel" -> gson.fromJson(jsonData, TodoModel::class.java)
                        "NoteModel" -> gson.fromJson(jsonData, NoteModel::class.java)
                        else -> null
                    }

                    val success = entity?.let { handleUpdateOperation(it) }
                    success ?: false
                }

                Operation.DELETE -> {
                    val success = handleDeleteOperation(jsonData, entityType)
                    success
                }

                Operation.TOGGLE -> {
                    val success = handleToggleOperation(jsonData, entityType)
                    success
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun deletePendingRequest(request: PendingRequestEntity) {
        runBlocking {
            pendingRequestRepository.deletePendingRequest(request)
        }
    }

    private suspend fun handleCreateOperation(entity: EntityModel): Boolean {
        return when (entity) {
            is TodoModel -> {
                val response = todoRepository.createTodo(
                    entity.title,
                    entity.theme.removePrefix("#"),
                    entity.reminder,
                    entity.description,
                    entity.state
                )

                handleApiResponse(response, entity.id, todoRepository::deleteTodoLocalDatabase)
            }
            is NoteModel -> {
                val response = noteRepository.createNote(
                    entity.title,
                    entity.message,
                    entity.tags,
                    entity.theme,
                    entity.is_trashed
                )

                handleApiResponse(response, entity.id, noteRepository::deleteNoteLocalDatabase)
            }

            else -> {
                false
            }
        }
    }

    private suspend fun handleUpdateOperation(entity: EntityModel): Boolean {
        return when (entity) {
            is TodoModel -> {
                val response = todoRepository.updateTodo(
                    entity.id,
                    entity.title,
                    entity.theme.removePrefix("#"),
                    entity.reminder,
                    entity.description,
                )

                handleApiResponse(response)
            }
            is NoteModel -> {
                val response = noteRepository.updateNoteById(
                    entity.id,
                    entity.title,
                    entity.message,
                    entity.tags,
                    entity.theme,
                    entity.is_trashed,
                )

                handleApiResponse(response)
            }

            else -> {
                false
            }
        }
    }

    private suspend fun handleDeleteOperation(idEntity: String, entityType: String): Boolean {
        return when (entityType) {
            "TodoModel" -> {
                when (todoRepository.deleteTodoById(idEntity)) {
                    is ApiResponse.Success -> {
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            "NoteModel" -> {
                when (noteRepository.deleteNoteById(idEntity)) {
                    is ApiResponse.Success -> {
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            else -> {
                false
            }
        }
    }

    private suspend fun handleToggleOperation(idEntity: String, entity: String): Boolean {
        return when (entity) {
            "TodoModel" -> {
                when (todoRepository.toggleTodoState(idEntity)) {
                    is ApiResponse.Success -> {true}
                    else -> {false}
                }
            }
            "NoteModel" -> {
                when (noteRepository.toggleTrashNoteById(idEntity)) {
                    is ApiResponse.Success -> {true}
                    else -> {false}
                }
            }

            else -> {false}
        }
    }

    private suspend fun <T> handleApiResponse(
        response: ApiResponse<T>,
        entityId: String,
        deleteLocalDatabase: suspend (String) -> Unit
    ): Boolean {
        return when (response) {
            is ApiResponse.Success -> {
                deleteLocalDatabase(entityId)
                true
            }
            else -> false
        }
    }

    private fun <T> handleApiResponse(
        response: ApiResponse<T>
    ): Boolean {
        return when (response) {
            is ApiResponse.Success -> true
            else -> false
        }
    }

    companion object {
        val syncStatusLiveData = MutableLiveData<SyncStatus>()
    }
}

enum class SyncStatus {
    INITIAL,
    IN_PROGRESS,
    COMPLETED
}
