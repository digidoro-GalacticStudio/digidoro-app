package com.galacticstudio.digidoro.network.dto.todo

import java.util.Calendar

data class RequestTodo(
    val title: String,
    val description: String,
    val theme: String,
    val reminder: Calendar
)

data class RequestChangeTodoTheme(
    val theme: String
)
