package com.galacticstudio.digidoro.data

import java.util.Date

//TODO Complete all attributes
data class TodoModel(
    val title: String,
    val theme: String,
    val createdAt: Date? = null,
    val state: Boolean ?= false,
)
