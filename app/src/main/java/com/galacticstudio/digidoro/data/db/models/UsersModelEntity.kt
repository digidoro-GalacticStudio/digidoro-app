package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.data.db.converters.DateConverter
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter
import java.util.Date

@Entity(tableName = "users")
data class UsersModelEntity(
    @PrimaryKey
    var _id: String,
    var firstname: String,
    var lastname: String,
    var username: String,
    var email: String,
    var phone_number: String,
    var password_hash: String,

    @TypeConverters(DateConverter::class)
    var date_birth: Date,

    @TypeConverters(ListStringConverter::class)
    var roles: List<String>,
    var salt: String,

    @TypeConverters(ListStringConverter::class)
    var tokens: List<String>,
    var profile_pic: String?,
    var level: String,
    var daily_score: Int,
    var weekly_score: Int,
    var monthly_score: Int,
    var total_score: Int,
    var createdAt: Date,
    var updatedAt: Date
)
