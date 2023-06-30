package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note")
data class NoteModel(
    @PrimaryKey
    val _id: String,
    val user_id: String,
    val title: String,
    val message: String,
    val tags: List<String>,
    val theme: String,
    val is_trashed: Boolean,
    val createdAt: String,
    val updatedAt: String
)