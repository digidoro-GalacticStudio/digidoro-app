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
    private val database: DigidoroDataBase,
    private var context: Context
) {
    private val todoDao = database.TodoDao()
    private val requestDao = database.PendingRequestDao()

    // Retrieve the application instance from the current context

    suspend fun insertIntoDataBase(): ApiResponse<String>{
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context = context)){
                val apiResponse = todoService.getTodos()
                todoDao.insertAll(apiResponse.data.toTodosModelEntity())
                "Inserted successfully"
            } else "could not insert into database"
            response
        }
    }
        suspend fun getAllTodo(
        sortBy: String ?= null,
        order: String ?= null,
        page: Int ?= null,
        limit: Int ?= null,
        populate: String ?= null,
    ): ApiResponse<List<TodoModel>>{
        return handleApiCall {
//            val response = if(CheckInternetConnectivity(context = context)){
//                todoService.getTodos(
//                    sortBy, order, page, limit, populate
//                ).toTodosModel()
//            } else todoDao.getAllTodo().toListTodosModel()
            val response =todoDao.getAllTodo().toListTodosModel()

            response
        }
    }

    suspend fun createTodo(
        title: String,
        theme: String,
        reminder: Date,
        description: String = "",
    ): ApiResponse<TodoModel> {
        return handleApiCall {
            if(CheckInternetConnectivity(context)){
                val request = RequestTodo(
                    title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                )

                val response = todoService.createTodo(request).toTodoModel()

                //Update the room database with the new To-do
                val requestRoom = TodoItemModelEntity(
                    _id = response.id,
                    title = response.title, description = response.description, theme = response.theme, reminder = reminder
                )
                todoDao.insertTodo(requestRoom).toTodosModel()


                response

            } else{
                val request = TodoItemModelEntity(
                    title = title, description = description, theme = "#$theme", reminder = reminder
                )


                val jsonString = Gson().toJson(request.toTodosModel()) // Convierte el objeto Note en un String JSON

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
    ): ApiResponse<TodoModel>{
        return handleApiCall {

            if(CheckInternetConnectivity(context)){
                val request = RequestTodo(title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder))

                Log.d("MyErrors", "theme : "+theme)
                todoDao.updateTodoById(
                    id, title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                ).toTodosModel()


                todoService.updateTodo(
                    id = id,
                    request = request
                ).toTodoModel()



            }
            else {
                val pendingEntity = TodoModel(id, title, description, "#$theme")
                val jsonString = Gson().toJson(pendingEntity)

                // Check if there is a previous request and update it
                val existingRequest = requestDao.getAllPendingRequests().find { it.data.contains(id) }

                val operation = if (existingRequest?.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                // Delete the existing request
                existingRequest?._id?.let {
                    Log.d("MyErrors", "::: [removiendo] : "+existingRequest)
                    requestDao.deletePendingRequest(it) }


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

    suspend fun toggleTodoState(id: String): ApiResponse<TodoModel>{
        return  handleApiCall {
            if(CheckInternetConnectivity(context)){

                val response = todoService.toggleTodoDone(id = id).toTodoModel()
                Log.d("MyErrors", "::: [GO here : " + response)

                todoDao.toggleStatusByStatus(id = id, response.state).toTodosModel()
                response
            }
            else{

                val existingRequest = requestDao.getAllPendingRequests().find { it.data.contains(id) }
//                        && (it.operation == Operation.TOGGLE)

                if (existingRequest == null || (existingRequest.operation == Operation.TOGGLE)) {
                        Log.d("MyErrors", "::: [TOOGEAR.data] : " + existingRequest)
                    if (existingRequest?.operation == Operation.TOGGLE) {
                        requestDao.deletePendingRequest(existingRequest._id) //Remove the previous request
                    }
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.TOGGLE,
                            data = id,
                            entityModel = "TodoModel"
                        )
                    )
                } else {
                    Log.d("MyErrors", "::: [existingRequest.data] : " + existingRequest.data)
                    requestDao.deletePendingRequest(existingRequest._id) //Remove the previous request

                    val converted = Gson().fromJson(existingRequest.data, TodoModel::class.java)
                    Log.d("MyErrors", "::: [removiendo antigua converted] : " + converted)

                    val pendingEntity = TodoModel(converted.id, converted.title, converted.description, converted.theme, state = !converted.state)
                    val jsonString = Gson().toJson(pendingEntity)
                    val operation = if (existingRequest.operation == Operation.CREATE) Operation.CREATE else Operation.UPDATE

                    // Insert or update the lastest request
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = operation,
                            data = jsonString,
                            entityModel = "TodoModel"
                        )
                    )
                }

                existingRequest?._id?.let {




                } ?: run {

                }

                todoDao.toggleStatusById(id = id).toTodosModel()
            }
        }
    }
    suspend fun deleteTodoById(id: String): ApiResponse<TodoModel>{
        return handleApiCall {
            if(CheckInternetConnectivity(context)) {
                val response = todoService.deleteNoteById(id = id).toTodoModel()
                todoDao.deleteTodoById(id)
                response
            }
            else {

                val existingRequest = requestDao.getAllPendingRequests().find { it.data.contains(id) }

//                if (existingRequest?.operation == Operation.CREATE) {
//                    requestDao.deletePendingRequest(existingRequest._id)
//                } else if (existingRequest?.operation == Operation.UPDATE) {
//                    requestDao.deletePendingRequest(existingRequest._id)
//                    requestDao.insertPendingRequest(
//                        PendingRequestEntity(
//                            operation = Operation.DELETE,
//                            data = id,
//                            entityModel = "TodoModel"
//                        )
//                    )
//                } else if (existingRequest==null) {
//                    requestDao.insertPendingRequest(
//                        PendingRequestEntity(
//                            operation = Operation.DELETE,
//                            data = id,
//                            entityModel = "TodoModel"
//                        )
//                    )
//                }

                existingRequest?._id?.let {
                    Log.d("MyErrors", "::: [removiendo antigua] : " + existingRequest)
                    requestDao.deletePendingRequest(it)
                }

                if (existingRequest?.operation == Operation.UPDATE || existingRequest==null) {
                    requestDao.insertPendingRequest(
                        PendingRequestEntity(
                            operation = Operation.DELETE,
                            data = id,
                            entityModel = "TodoModel"
                        )
                    )
                }

//
//                    val existingRequest = requestDao.getAllPendingRequests().find { it.data.contains(id) }
//
//                existingRequest?.let {
//                    requestDao.deletePendingRequest(it._id)
//                    if (it.operation == Operation.UPDATE) {
//                        requestDao.insertPendingRequest(
//                            PendingRequestEntity(
//                                operation = Operation.DELETE,
//                                data = id,
//                                entityModel = "TodoModel"
//                            )
//                        )
//                    }
//                } ?: run {
//                    requestDao.insertPendingRequest(
//                        PendingRequestEntity(
//                            operation = Operation.DELETE,
//                            data = id,
//                            entityModel = "TodoModel"
//                        )
//                    )
//                }


                todoDao.deleteTodoById(id).toTodosModel()
            }
        }
    }

    suspend fun deleteTodoLocalDatabase(id: String): ApiResponse<TodoModel>{
        return handleApiCall {
            todoDao.deleteTodoById(id).toTodosModel()
        }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            todoDao.deleteAllTodo()

            "Deleted successfully"
        }
    }
}