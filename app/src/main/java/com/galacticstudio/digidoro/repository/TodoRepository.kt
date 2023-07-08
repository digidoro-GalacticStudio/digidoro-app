package com.galacticstudio.digidoro.repository

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
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
                val apiResponse = todoService.getTodos(
                    sortBy, order, page, limit, populate
                )
//                todoDao.insertAll(apiResponse.data.toTodosModelEntity())

                apiResponse.toTodosModel()

            } else {
                todoDao.getAllTodo().toTodosModel()
            }

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
            val request = RequestTodo(
                title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder)
            )
            todoService.createTodo(request).toTodoModel()
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
            val request = RequestTodo(title, description, "#$theme", DateUtils.dateKTToDateAPI(reminder))
            todoService.updateTodo(
                id = id,
                request = request
                ).toTodoModel()
        }
    }

    suspend fun toggleTodoState(id: String): ApiResponse<TodoModel>{
        return  handleApiCall {
            todoService.toggleTodoDone(id = id).toTodoModel()
        }
    }
    suspend fun deleteTodoById(id: String): ApiResponse<TodoModel>{
        return handleApiCall {
            todoService.deleteNoteById(id = id).toTodoModel()
        }
    }

    suspend fun deleteAll(): ApiResponse<String>{
        return handleApiCall {
            todoDao.deleteTodo()

            "Deleted successfully"
        }
    }
}