package com.galacticstudio.digidoro.network.dto.todo

import java.util.Date

data class RequestTodo(
    val title: String,
    val description: String,
    val theme: String,
    val reminder: String
)

data class RequestChangeTodoTheme(
    val theme: String
)
