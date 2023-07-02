package com.galacticstudio.digidoro.network.dto.ranking

import com.google.gson.annotations.SerializedName

data class RankingResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserInformationData
)

data class RankingListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<UserInformationData>
)

data class UpdateScoreResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)

data class UserInformationData(
    @SerializedName("_id") val id: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("username") val username: String,
    @SerializedName("profile_pic") val profilePic: String,
    @SerializedName("level") val level: String,
    @SerializedName("daily_score") val dailyScore: Int,
    @SerializedName("weekly_score") val weeklyScore: Int,
    @SerializedName("monthly_score") val monthlyScore: Int,
    @SerializedName("total_score") val totalScore: Int
)
