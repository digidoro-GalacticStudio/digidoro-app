package com.galacticstudio.digidoro.network.dto.todo

import android.util.Log
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import com.google.gson.annotations.SerializedName
import java.util.Date

//data one todo
data class ResponseTodo(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TodoData,
    @SerializedName("error") val error: String,
)

//data multiple todo
data class ResponseAllTodo(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MutableList<TodoData>,
    @SerializedName("error") val error: String,
    @SerializedName("totalCount") val totalCount: Int,

    )

data class TodoData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("theme") val theme: String,
    @SerializedName("reminder") val reminder: Date,
    @SerializedName("is_completed") val is_completed: Boolean,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date,
)

//Convert response to todo model
fun ResponseTodo.toTodoModel(): TodoModel {
    return TodoModel(
        id = data.id,
        title = data.title,
        description = data.description,
        theme = data.theme,
        createdAt = data.createdAt,
        state = data.is_completed,
        reminder = data.reminder
    )
}

//Convert response to todo s model
fun ResponseAllTodo.toTodosModel(): MutableList<TodoModel>{
    val response = data.mapIndexed{ _, element ->
        TodoModel(
            id = element.id,
            title = element.title,
            description = element.description,
            theme = element.theme,
            createdAt = element.createdAt,
            state = element.is_completed,
            reminder = element.reminder
        )
    }

    return response.toMutableList()
}

//Convert response to todo item model entity
fun MutableList<TodoData>.toTodosModelEntity(): MutableList<TodoItemModelEntity>{
    return map{ element ->
        TodoItemModelEntity(
            _id = element.id,
            user_id = element.user_id,
            title = element.title,
            description = element.description,
            theme = element.theme,
            reminder = element.reminder,
            is_completed = element.is_completed,
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }.toMutableList()
}


//Convert response to todo s model
fun List<TodoItemModelEntity>.toTodosModel(): MutableList<TodoModel>{

   return map{element ->
        TodoModel(
            id = element._id,
            title = element.title,
            description = element.description,
            theme = element.theme,
            createdAt = element.createdAt,
            state = element.is_completed,
            reminder = element.reminder
        )
    }.toMutableList()
}