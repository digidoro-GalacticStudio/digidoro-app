package com.galacticstudio.digidoro.network.dto.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ProfileData
)

data class ProfileData(
    @SerializedName("_id") val id: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("username") val username: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("date_birth") val dateOfBirth: String,
    @SerializedName("profile_pic") val profilePic: String,
    @SerializedName("level") val level: String,
    @SerializedName("daily_score") val dailyScore: Int,
    @SerializedName("weekly_score") val weeklyScore: Int,
    @SerializedName("monthly_score") val monthlyScore: Int,
    @SerializedName("total_score") val totalScore: Int
)
