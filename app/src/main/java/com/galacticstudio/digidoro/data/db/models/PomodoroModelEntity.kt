package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "pomodoro")
data class PomodoroModelEntity(
    @PrimaryKey
    val _id: String,
    val name: String,
    val user_id: String,
    val sessions_completed: Int,
    val total_sessions: Int,
    val theme: String,
    val createdAt: Date,
    val updatedAt: Date
)
