package com.galacticstudio.digidoro.data.api

import java.util.Calendar
import java.util.Date

//TODO Complete all attributes
data class TodoModel(
    val id: String = "",
    val title: String,
    val description: String,
    val theme: String,
    val createdAt: Date = Calendar.getInstance().time,
    val state: Boolean = false,
    val reminder: Date = Calendar.getInstance().time
)
