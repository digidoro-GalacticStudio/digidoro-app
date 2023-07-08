package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galacticstudio.digidoro.util.DateUtils
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "todoitem")
data class TodoItemModelEntity(
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString(),
    var user_id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var theme: String,
    var reminder: Date,
    var is_completed: Boolean = false,
    var createdAt: Date = Calendar.getInstance().time,
    var updatedAt: Date = Calendar.getInstance().time
)