package com.galacticstudio.digidoro.data

import java.util.Date

//TODO Complete all attributes
data class UserModel(
    val name: String,
    val lastname: String,
    val email: String,
    val phone_number: String,
    val password_hash: String,
    val date_birth: Date,
    val profile_pic: String = "https://i.imgur.com/GvsgVco.jpeg",
    val level: String = "Dreamer",
    val daily_score: Int = 0,
    val weekly_score: Int = 0,
    val monthly_score: Int = 0,
    val total_score: Int = 0
)
