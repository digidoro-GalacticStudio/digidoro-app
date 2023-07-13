package com.galacticstudio.digidoro.data.db.converters

import androidx.room.TypeConverter
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConverter {
    @TypeConverter
    fun fromList(list: List<String>): String?{
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromList(list: String?): List<String>?{
        return list?.split(",")?.map{ it.trim()}
    }

}