package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter


@Entity(tableName = "folder")
data class FolderModelEntity(
    @PrimaryKey
    var _id: String,
    var user_id: String,
    var name: String,
    var theme: String,

    @TypeConverters(ListStringConverter::class)
    var notes_id: List<String>,
    var createdAt: String,
    var updatedAt: String
)