package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter


@Entity(tableName = "note")
data class NoteModelEntity(
    @PrimaryKey
    var _id: String,
    var user_id: String,
    var title: String,
    var message: String,

    @TypeConverters(ListStringConverter::class)
    var tags: List<String>,
    var theme: String,
    var is_trashed: Boolean,
    var createdAt: String,
    var updatedAt: String
)