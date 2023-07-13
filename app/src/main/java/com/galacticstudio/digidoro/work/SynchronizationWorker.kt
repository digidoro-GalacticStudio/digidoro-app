package com.galacticstudio.digidoro.work

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.api.EntityModel
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.network.ApiResponse
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
    private val thisContext = context

    override suspend fun doWork(): Result  {
        startForegroundService()

        val app = applicationContext as Application
        todoRepository = app.todoRepository
        pendingRequestRepository = app.pendingRequestRepository

        val pendingRequests = getPendingRequestsFromDatabase()

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
                        //"NoteModel" -> gson.fromJson(jsonData, NoteModel::class.java)
                        else -> null
                    }
                    val success = entity?.let { handleCreateOperation(it) }
                    success ?: false
                }

                Operation.UPDATE -> {
                    val entity: EntityModel? = when (entityType) {
                        "TodoModel" -> gson.fromJson(jsonData, TodoModel::class.java)
                        //"NoteModel" -> gson.fromJson(jsonData, NoteModel::class.java)
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
                    val success = handleToggleOperation(jsonData)
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
                )

                when (response) {
                    is ApiResponse.Success -> {
                        todoRepository.deleteTodoLocalDatabase(entity.id)
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

                when (response) {
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

            else -> {
                false
            }
        }
    }


    private suspend fun handleToggleOperation(idEntity: String): Boolean {
        return when (todoRepository.toggleTodoState(idEntity)) {
            is ApiResponse.Success -> {

                true
            }

            else -> {
                false
            }
        }
    }
}