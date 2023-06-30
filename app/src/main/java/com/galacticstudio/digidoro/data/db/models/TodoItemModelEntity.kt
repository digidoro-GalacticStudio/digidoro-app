package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todoitem")
data class TodoItemModelEntity(
    @PrimaryKey
    var _id: String,
    var user_id: String,
    var title: String,
    var description: String,
    var theme: String,
    var reminder: Date,
    var is_completed: Boolean,
    var createdAt: Date,
    var updatedAt: Date
)