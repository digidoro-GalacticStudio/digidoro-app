package com.galacticstudio.digidoro.repository

import android.content.Context
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
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
import java.util.Date


class TodoRepository(
    private val todoService: TodoService,
    private val database: DigidoroDataBase,
    private var context: Context
) {
    private val todoDao = database.TodoDao()

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
            val response = if(CheckInternetConnectivity(context = context)){
                todoService.getTodos(
                    sortBy, order, page, limit, populate
                ).toTodosModel()
            } else todoDao.getAllTodo().toListTodosModel()

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
                todoService.createTodo(request).toTodoModel()
            }
            else{
                val request = TodoItemModelEntity(
                    title = title, description = description, theme = "#$theme", reminder = reminder
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
                todoService.updateTodo(
                    id = id,
                    request = request
                ).toTodoModel()

            }
            else {
                todoDao.updateTodoById(
                    id, title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
                ).toTodosModel()
            }
        }
    }

    suspend fun toggleTodoState(id: String): ApiResponse<TodoModel>{
        return  handleApiCall {
            if(CheckInternetConnectivity(context)){
                todoService.toggleTodoDone(id = id).toTodoModel()
            }
            else{
                todoDao.toggleStatusById(id = id).toTodosModel()
            }
        }
    }
    suspend fun deleteTodoById(id: String): ApiResponse<TodoModel>{
        return handleApiCall {
            if(CheckInternetConnectivity(context)) todoService.deleteNoteById(id = id).toTodoModel()
            else todoDao.deleteTodoById(id).toTodosModel()
        }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            todoDao.deleteAllTodo()

            "Deleted successfully"
        }
    }
}