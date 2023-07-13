package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.todo.RequestChangeTodoTheme
import com.galacticstudio.digidoro.network.dto.todo.RequestTodo
import com.galacticstudio.digidoro.network.dto.todo.ResponseAllTodo
import com.galacticstudio.digidoro.network.dto.todo.ResponseTodo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TodoService {
    @POST("api/todoItem/own")
    suspend fun createTodo(
        @Body todo: RequestTodo
    ): ResponseTodo

    @GET("api/todoItem/own")
    suspend fun getTodos(
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("populateFields") populateFields: String? = null,
    ) : ResponseAllTodo

    @GET("api/todoItem/own/{id}")
    suspend fun getTodo(
        @Path("id") id: String
    ) : ResponseTodo

    @PATCH("api/todoItem/own/completed/{id}")
    suspend fun toggleTodoDone(
        @Path("id") id: String
    ) : ResponseTodo

    @PATCH("api/todoItem/own/theme/{id}")
    suspend fun toggleTheme(
        @Path("id") id: String,
        @Body changeTodoTheme: RequestChangeTodoTheme
    ) : ResponseTodo

    @PATCH("api/todoItem/own/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Body request: RequestTodo
    ) : ResponseTodo

    @DELETE("api/todoItem/own/{id}")
    suspend fun deleteNoteById(
        @Path("id") id: String
    ) : ResponseTodo
}