package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "pomodoro")
data class PomodoroModelEntity(
    @PrimaryKey
    val _id: String = UUID.randomUUID().toString(),
    val user_id: String = UUID.randomUUID().toString(),
    val name: String,
    val sessions_completed: Int,
    val total_sessions: Int,
    val theme: String,
    val createdAt: Date = Calendar.getInstance().time,
    val updatedAt: Date = Calendar.getInstance().time
)
