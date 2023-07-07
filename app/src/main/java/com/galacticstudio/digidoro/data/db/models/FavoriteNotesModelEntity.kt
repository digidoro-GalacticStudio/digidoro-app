package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.ListNoteModelEntityConverter


@Entity(tableName = "favoritenotes")
data class FavoriteNotesModelEntity (
    @PrimaryKey
    var _id: String,
    var user_id: String,

    @TypeConverters(ListNoteModelEntityConverter::class)
    var notes_id: List<NoteModelEntity>
)
