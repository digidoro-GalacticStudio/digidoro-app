package com.galacticstudio.digidoro.data.db.converters

import androidx.room.TypeConverter
import java.sql.Timestamp
import java.util.Date


class DateConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date?{
        return timestamp?.let {
            Date(it)
        }
    }
}