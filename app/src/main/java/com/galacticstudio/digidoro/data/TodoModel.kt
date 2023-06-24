package com.galacticstudio.digidoro.data

import java.util.Calendar
import java.util.Date

//TODO Complete all attributes
data class TodoModel(
    val id: String ?= null,
    val title: String,
    val description: String,
    val theme: String,
    val createdAt: Date ?= null,
    val state: Boolean ?= false,
    val reminder: Date ?= null
)
