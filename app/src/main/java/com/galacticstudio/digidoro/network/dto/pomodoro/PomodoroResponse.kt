package com.galacticstudio.digidoro.network.dto.pomodoro

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PomodoroResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PomodoroData,
)

data class PomodoroListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<PomodoroData>,
)

data class PomodoroData(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("sessions_completed") val sessionsCompleted: Int,
    @SerializedName("total_sessions") val totalSessions: Int,
    @SerializedName("theme") val theme: String,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date,
)