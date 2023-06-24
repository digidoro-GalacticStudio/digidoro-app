package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.data.TodoModel
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
import com.galacticstudio.digidoro.network.dto.todo.ResponseTodo
import com.galacticstudio.digidoro.network.dto.todo.TodoData
import com.galacticstudio.digidoro.network.dto.todo.toTodoModel
import com.galacticstudio.digidoro.network.dto.todo.toTodosModel
import com.galacticstudio.digidoro.network.service.TodoService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar


class TodoRepository(
    private val todoService: TodoService
) {


    suspend fun createTodo(
        title: String,
        theme: String,
        reminder: Calendar,
        description: String = "",
    ): ApiResponse<TodoModel> {
        try {
            val response = todoService.createTodo(RequestTodo(
                title = title,
                description = description,
                theme = theme,
                reminder = reminder
            ));

            val todoResponseData = response.toTodoModel()

            return ApiResponse.Success(todoResponseData)
        }
        catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, ResponseTodo::class.java).message

            val errorMessage = if (response is String) response
            else {
                val messageJson = response as? Map<*, *>
                messageJson?.get("error") as? String ?: "Unknown error"
            }

            return  ApiResponse.ErrorWithMessage(errorMessage)
        }
        catch (e: IOException){
            return ApiResponse.ErrorWithMessage("No internet connection")
        }
        catch (e: Exception){
            return ApiResponse.Error(e)
        }
    }

    suspend fun getAllTodo(
        sortBy: String ?= null,
        order: String ?= null,
        page: Int ?= null,
        limit: Int ?= null,
        populate: String ?= null,
    ): ApiResponse<List<TodoModel>>{
        try{
            val reponse = todoService.getTodos(sortBy, order, page, limit, populate)
            val todoReponse = reponse.toTodosModel()
            return ApiResponse.Success(todoReponse)

        }
        catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, ResponseTodo::class.java).message

            val errorMessage = if (response is String) response
            else {
                val messageJson = response as? Map<*, *>
                messageJson?.get("error") as? String ?: "Unknown error"
            }

            return  ApiResponse.ErrorWithMessage(errorMessage)
        }
        catch (e: IOException){
            return ApiResponse.ErrorWithMessage("No internet connection")
        }
        catch (e: Exception){
            return ApiResponse.Error(e)
        }
    }

    suspend fun deleteTodoById(id: String): ApiResponse<TodoModel>{
        try{
            val reponse =  todoService.deleteNoteById(id)
            val todoReponse = reponse.toTodoModel()
            return ApiResponse.Success(todoReponse)

        }
        catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, ResponseTodo::class.java).message

            val errorMessage = if (response is String) response
            else {
                val messageJson = response as? Map<*, *>
                messageJson?.get("error") as? String ?: "Unknown error"
            }

            return  ApiResponse.ErrorWithMessage(errorMessage)
        }
        catch (e: IOException){
            return ApiResponse.ErrorWithMessage("No internet connection")
        }
        catch (e: Exception){
            return ApiResponse.Error(e)
        }
    }

//
//    suspend fun changeTodoTheme(
//        theme: String
//    ): ApiResponse<TodoData>{
//
//    }
//
//    suspend fun toggleTodoState(): ApiResponse<TodoData>{
//
//    }
}