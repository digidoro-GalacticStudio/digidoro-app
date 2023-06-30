package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "todoitem")
@TypeConverters(DateConverter::class)
data class TodoItemModel(
    @PrimaryKey
    val _id: String,
    val user_id: String,
    val title: String,
    val description: String,
    val theme: String,
    val reminder: Date,
    val is_completed: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)