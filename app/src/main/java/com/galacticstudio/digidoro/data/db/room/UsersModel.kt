package com.galacticstudio.digidoro.data.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "users")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class UsersModel(
    @PrimaryKey
    val _id: String,
    val name: String,
    val lastname: String,
    val email: String,
    val phone_number: String,
    val password_hash: String,
    val date_birth: Date,
    val roles: List<String>,
    val salt: String,
    val tokens: List<String>,
    val profile_pic: String?,
    val level: String,
    val daily_score: Int,
    val weekly_score: Int,
    val monthly_score: Int,
    val total_score: Int,
    val createdAt: Date,
    val updatedAt: Date
)

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
