package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.data.db.models.Operation
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
import com.galacticstudio.digidoro.network.dto.todo.toListTodosModel
import com.galacticstudio.digidoro.network.dto.todo.toTodoModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModelEntity
import com.galacticstudio.digidoro.network.service.TodoService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall
import com.galacticstudio.digidoro.util.DateUtils
import com.google.gson.Gson
import java.util.Date


class TodoRepository(
    private val todoService: TodoService,
    database: DigidoroDataBase,
    private var context: Context
) {
    private val todoDao = database.TodoDao()
    private val requestDao = database.PendingRequestDao()

    suspend fun insertIntoDataBase(): ApiResponse<String> {
        return handleApiCall {
            val response = if (CheckInternetConnectivity(context = context)) {
                val apiResponse = todoService.getTodos()
                todoDao.insertAll(apiResponse.data.toTodosModelEntity())
                "Inserted successfully"
            } else "could not insert into database"
            response
        }
    }

    suspend fun getAllTodo(
        sortBy: String? = null,
        order: String? = null,
        page: Int? = null,
        limit: Int? = null,
        populate: String? = null,
    ): ApiResponse<List<TodoModel>> {
        return handleApiCall {
//            val response = if (CheckInternetConnectivity(context = context)) {
//                todoService.getTodos(
//                    sortBy, order, page, limit, populate
//                ).toTodosModel()
//            } else todoDao.getAllTodo().toListTodosModel()
            val response = todoDao.getAllTodo().toListTodosModel()

            response
        }
    }

    suspend fun createTodo(
        title: String,
        theme: String,
        reminder: Date,
        description: String = "",
        status: Boolean = false,
    ): ApiResponse<TodoModel> {
        return handleApiCall {
            if (CheckInternetConnectivity(context)) {
                val request = RequestTodo(
                    title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                )
                val response = todoService.createTodo(request).toTodoModel()

                //Update the room database with the new To-do
                val requestRoom = TodoItemModelEntity(
                    _id = response.id,
                    title = response.title,
                    description = response.description,
                    theme = response.theme,
                    reminder = reminder
                )
                todoDao.insertTodo(requestRoom)

                if (status) todoService.toggleTodoDone(response.id)

                response
            } else {
                val request = TodoItemModelEntity(
                    title = title, description = description, theme = "#$theme", reminder = reminder
                )

                val jsonString =
                    Gson().toJson(request.toTodosModel()) // Convert the to-do object to a JSON String
                // Create a pending request with the values of the new to-do object
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = Operation.CREATE,
                        data = jsonString,
                        entityModel = "TodoModel"
                    )
                )

                todoDao.insertTodo(request).toTodosModel()
            }
        }
    }

    suspend fun updateTodo(
        id: String,
        title: String,
        theme: String,
        reminder: Date,
        description: String = "",
    ): ApiResponse<TodoModel> {
        return handleApiCall {

            if (CheckInternetConnectivity(context)) {
                val request =
                    RequestTodo(title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder))

                // Update the local database with the update
                todoDao.updateTodoById(
                    id, title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                )

                todoService.updateTodo(
                    id = id,
                    request = request
                ).toTodoModel()

            } else {
                // Convert to json to be able to send it in my offline requests
                val jsonString = Gson().toJson(TodoModel(id, title, description, "#$theme"))

                // Check if there is a previous request and update it
                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(id) }
                val operation =
                    if (existingRequest?.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                // Delete the existing request
                existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                // Insert the updated request
                requestDao.insertPendingRequest(
                    PendingRequestEntity(
                        operation = operation,
                        data = jsonString,
                        entityModel = "TodoModel"
                    )
                )

                todoDao.updateTodoById(
                    id, title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                ).toTodosModel()
            }
        }
    }

    suspend fun toggleTodoState(id: String): ApiResponse<TodoModel> {
        return handleApiCall {
            if (CheckInternetConnectivity(context)) {
                val response = todoService.toggleTodoDone(id = id).toTodoModel()

                // Update the local database with the toggle action
                todoDao.toggleStatusByStatus(id = id, response.state).toTodosModel()
                response
            } else {
                val existingRequest = requestDao.getAllPendingRequests().find { it.data.contains(id) }

                if (existingRequest == null || (existingRequest.operation == Operation.TOGGLE)) {

                    // Remove the previous request if it was a toggle operation
                    if (existingRequest?.operation == Operation.TOGGLE)
                        requestDao.deletePendingRequest(existingRequest._id)

                    // Insert a new pending request for toggling the TodoModel with the specified id
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.TOGGLE,
                            data = id,
                            entityModel = "TodoModel"
                        )
                    )
                } else {
                    requestDao.deletePendingRequest(existingRequest._id) //Remove the previous request

                    // Convert the existing request data from JSON to a TodoModel object
                    val converted = Gson().fromJson(existingRequest.data, TodoModel::class.java)
                    // Create a new TodoModel with the toggled state and convert the updated TodoModel back to JSON
                    val pendingEntity = TodoModel(converted.id, converted.title, converted.description, converted.theme, state = !converted.state)
                    val jsonString = Gson().toJson(pendingEntity)
                    val operation = if (existingRequest.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                    // Insert or update the latest request
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = operation,
                            data = jsonString,
                            entityModel = "TodoModel"
                        )
                    )
                }

                // Update the local database with the toggle action
                todoDao.toggleStatusById(id = id).toTodosModel()
            }
        }
    }

    suspend fun deleteTodoById(id: String): ApiResponse<TodoModel> {
        return handleApiCall {
            if(CheckInternetConnectivity(context)) {
                val response = todoService.deleteNoteById(id = id).toTodoModel()
                // Update the local database with the update
                todoDao.deleteTodoById(id)
                response
            } else {

                val existingRequest =
                    requestDao.getAllPendingRequests().find { it.data.contains(id) }

                existingRequest?._id?.let { requestDao.deletePendingRequest(it) }

                if (existingRequest?.operation == Operation.UPDATE || existingRequest == null) {
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.DELETE,
                            data = id,
                            entityModel = "TodoModel"
                        )
                    )
                }

                todoDao.deleteTodoById(id).toTodosModel()
            }
        }
    }

    suspend fun deleteTodoLocalDatabase(id: String): ApiResponse<TodoModel>{
        return handleApiCall {
            todoDao.deleteTodoById(id).toTodosModel()
        }
    }

    suspend fun deleteAll(): ApiResponse<String> {
        return handleApiCall {
            todoDao.deleteAllTodo()

            "Deleted successfully"
        }
    }
}