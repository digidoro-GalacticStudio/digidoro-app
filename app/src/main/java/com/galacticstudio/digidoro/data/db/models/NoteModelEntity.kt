package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter
import java.util.Calendar
import java.util.Date
import java.util.UUID


@Entity(tableName = "note")
data class NoteModelEntity(
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString(),
    var user_id: String = UUID.randomUUID().toString(),
    var title: String,
    var message: String,

    @TypeConverters(ListStringConverter::class)
    var tags: List<String>,
    var theme: String,
    var is_trashed: Boolean = false,
    var createdAt: Date = Calendar.getInstance().time,
    var updatedAt: Date = Calendar.getInstance().time,
)