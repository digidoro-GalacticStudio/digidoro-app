package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
import com.galacticstudio.digidoro.network.dto.todo.ResponseTodo
import com.galacticstudio.digidoro.network.dto.todo.TodoData
import com.galacticstudio.digidoro.network.dto.todo.toTodoModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
import com.galacticstudio.digidoro.network.service.TodoService
import com.galacticstudio.digidoro.repository.utils.handleApiCall
import com.galacticstudio.digidoro.util.DateUtils
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar
import java.util.Date


class TodoRepository(
    private val todoService: TodoService
) {

        suspend fun getAllTodo(
        sortBy: String ?= null,
        order: String ?= null,
        page: Int ?= null,
        limit: Int ?= null,
        populate: String ?= null,
    ): ApiResponse<List<TodoModel>>{
        return handleApiCall {
            todoService.getTodos(
                sortBy, order, page, limit, populate
            ).toTodosModel()
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

}