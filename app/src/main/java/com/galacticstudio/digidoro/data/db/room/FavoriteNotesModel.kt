package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "favoritenotes")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class FavoriteNotesModel (
    @PrimaryKey
    val _id: String,
    val user_id: String,
    val notes_id: List<String>
)
