package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListNoteModelEntityConverter
import java.util.UUID


@Entity(tableName = "favoritenotes")
data class FavoriteNotesModelEntity (
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString(),
    var user_id: String = UUID.randomUUID().toString(),

    @TypeConverters(ListNoteModelEntityConverter::class)
    var notes_id: List<NoteModelEntity>
)
