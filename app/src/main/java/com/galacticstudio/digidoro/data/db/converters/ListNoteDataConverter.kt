package com.galacticstudio.digidoro.data.db.converters

import androidx.room.TypeConverter
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListNoteModelEntityConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromJsonToListNoteModelEntity(json: String): List<NoteModelEntity> {
        val type = object : TypeToken<List<NoteModelEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromListNoteModelEntityToJson(list: List<NoteModelEntity>): String {
        return gson.toJson(list)
    }
}