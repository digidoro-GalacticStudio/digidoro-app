package com.galacticstudio.digidoro.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.api.EntityModel
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.repository.RequestRepository
import com.galacticstudio.digidoro.repository.TodoRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private lateinit var pendingRequestRepository: RequestRepository
    private lateinit var todoRepository: TodoRepository

    override fun doWork(): Result = runBlocking {

//        Log.d("MyErrors", "Reciviend data !!!")
        val app = applicationContext as Application
        todoRepository = app.todoRepository
        pendingRequestRepository = app.pendingRequestRepository
//        Log.d("MyErrors", "FInishing data !!!")



//        todoRepository = todoRepositoryClassName?.let { Class.forName(it).newInstance() } as TodoRepository


        val pendingRequests = getPendingRequestsFromDatabase()

        Log.d("MyErrors", "pendingRequests: " + pendingRequests)

        for (request in pendingRequests) {
//            Log.d("MyErrors", "FOR : " + request)
            val success = syncWithApi(request)

            if (success) {
                deletePendingRequest(request)
            } else {
                Log.d("MyErrors", "[[ERROR] : " + pendingRequests)
                deletePendingRequest(request)
                // Maneja el error apropiadamente
            }
        }

        Result.success()
    }

    private fun getPendingRequestsFromDatabase(): List<PendingRequestEntity> {
//        val dao = database.PendingRequestDao()

        return runBlocking {
            when (val response = pendingRequestRepository.getAllRequest()) {
                is ApiResponse.Success -> {
                    response.data
                    //emptyList()
                }

                else -> {emptyList()}
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
            val response = pendingRequestRepository.deletePendingRequest(request)
        }
    }

    private suspend fun handleCreateOperation(entity: EntityModel): Boolean {
        return when (entity) {
            is TodoModel -> {
                val response = todoRepository.createTodo(
                    entity.title,
                    entity.theme.removePrefix("#") ,
                    entity.reminder,
                    entity.description,
                )

                when (response) {
                    is ApiResponse.Success -> {
//                        todoRepository.updateOnlyIDTodo(entity.id, response.data.id)
                        todoRepository.deleteTodoLocalDatabase(entity.id)
                        true
                    }
                    else -> {false}
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
                Log.d("MyErrors", "Enter gere "+entity.theme)
                val response = todoRepository.updateTodo(
                    entity.id,
                    entity.title,
                    entity.theme.removePrefix("#") ,
                    entity.reminder,
                    entity.description,
                )
                Log.d("MyErrors", "RESPONSE "+response)

                when (response) {
                    is ApiResponse.Success -> {
                        true
                    }
                    else -> {false}
                }
            }
            else -> {
                Log.d("MyErrors", "GOOO right here ")
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
                    else -> {false}
                }
            }
            else -> {
                false
            }
        }
    }


    private suspend fun handleToggleOperation(idEntity: String): Boolean {
        Log.d("MyErrors", "Enter gere "+idEntity)

        return  when (todoRepository.toggleTodoState(idEntity)) {
            is ApiResponse.Success -> {

                true
            }
            else -> {false}
        }
    }
}

