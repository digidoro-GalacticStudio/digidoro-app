package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro")
data class PomodoroModel(
    @PrimaryKey
    val _id: String,
    val name: String,
    val user_id: String,
    val pomodoro: Int,
    val breakTime: Int,
    val long_break: Int,
    val break_pomodoro: Int,
    val pomodoro_day: Int,
    val theme: String,
    val reminder: Boolean,
    val createdAt: String,
    val updatedAt: String
)
