package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favoritenotes")
data class FavoriteNotesModel (
    @PrimaryKey
    val _id: String,
    val user_id: String,
    val notes_id: List<String>
)
