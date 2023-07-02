package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
import com.galacticstudio.digidoro.network.dto.todo.toTodoModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModelEntity
import com.galacticstudio.digidoro.network.service.TodoService
import com.galacticstudio.digidoro.repository.utils.handleApiCall
import com.galacticstudio.digidoro.util.DateUtils
import java.util.Date


class TodoRepository(
    private val todoService: TodoService,
    private val database: DigidoroDataBase
) {
    private val todoDao = database.TodoDao()
        suspend fun getAllTodo(
        sortBy: String ?= null,
        order: String ?= null,
        page: Int ?= null,
        limit: Int ?= null,
        populate: String ?= null,
    ): ApiResponse<List<TodoModel>>{
        return handleApiCall {
            val response = todoService.getTodos(
                sortBy, order, page, limit, populate
            )

            todoDao.insertAll(response.toTodosModelEntity())

            response.toTodosModel()
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