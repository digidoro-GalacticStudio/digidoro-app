package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "folder")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class FolderModel(
    @PrimaryKey
    val _id: String,
    val user_id: String,
    val name: String,
    val theme: String,
    val notes_id: List<String>,
    val createdAt: String,
    val updatedAt: String
)