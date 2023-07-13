package com.galacticstudio.digidoro.network.dto.pomodoro

import com.galacticstudio.digidoro.data.db.models.PomodoroModelEntity
import com.google.gson.annotations.SerializedName
import java.util.Date

data class SimpleResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)

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

fun List<PomodoroData>.toListPomdoroModelEntity(): List<PomodoroModelEntity>{
    return map{ element ->
        PomodoroModelEntity(
            _id = element.id,
            name = element.name,
            user_id = element.user_id,
            sessions_completed = element.sessionsCompleted,
            total_sessions = element.totalSessions,
            theme = element.theme,
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }.toList()
}

fun List<PomodoroModelEntity>.toListPomodoroData(): List<PomodoroData>{
    return map{ element ->
        PomodoroData(
            id = element._id,
            name = element.name,
            user_id = element.user_id,
            sessionsCompleted = element.sessions_completed,
            totalSessions = element.total_sessions,
            theme = element.theme,
            createdAt = element.createdAt,
            updatedAt = element.updatedAt
        )
    }.toList()
}
fun PomodoroModelEntity.toPomodoroData(): PomodoroData{
    return PomodoroData(
            id = this._id,
            name = this.name,
            user_id = this.user_id,
            sessionsCompleted = this.sessions_completed,
            totalSessions = this.total_sessions,
            theme = this.theme,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
}