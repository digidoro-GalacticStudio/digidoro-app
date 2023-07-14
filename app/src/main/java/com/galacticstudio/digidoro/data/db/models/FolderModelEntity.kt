package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListNoteModelEntityConverter
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter
import com.galacticstudio.digidoro.util.DateUtils
import java.util.Calendar
import java.util.UUID


@Entity(tableName = "folder")
data class FolderModelEntity(
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString(),
    var user_id: String = UUID.randomUUID().toString(),
    var name: String,
    var theme: String,

    @TypeConverters(ListNoteModelEntityConverter::class)
    var notes_id: List<NoteModelEntity>,

    var createdAt: String = DateUtils.parseDateToString(Calendar.getInstance().time),
    var updatedAt: String = DateUtils.parseDateToString(Calendar.getInstance().time)
)