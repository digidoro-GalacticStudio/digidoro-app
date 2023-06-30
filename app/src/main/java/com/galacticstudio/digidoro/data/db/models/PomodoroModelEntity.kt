package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro")
data class PomodoroModelEntity(
    @PrimaryKey
    var _id: String,
    var name: String,
    var user_id: String,
    var pomodoro: Int,
    var breakTime: Int,
    var long_break: Int,
    var break_pomodoro: Int,
    var pomodoro_day: Int,
    var theme: String,
    var reminder: Boolean,
    var createdAt: String,
    var updatedAt: String
)
