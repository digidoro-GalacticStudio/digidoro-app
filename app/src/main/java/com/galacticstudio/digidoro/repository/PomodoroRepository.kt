package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroData
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroRequest
import com.galacticstudio.digidoro.network.service.PomodoroService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class PomodoroRepository(private val pomodoroService: PomodoroService) {
    suspend fun getAllPomodoros(): ApiResponse<List<PomodoroData>> {
        return handleApiCall { pomodoroService.getAllPomodoros().data }
    }

    suspend fun getPomodoroById(pomodoroId: String): ApiResponse<PomodoroData> {
        return handleApiCall { pomodoroService.getPomodoroById(pomodoroId).data }
    }

    suspend fun createPomodoro(
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ): ApiResponse<PomodoroData> {
        val request = PomodoroRequest(name, sessionsCompleted, totalSessions, theme)
        return handleApiCall { pomodoroService.createPomodoro(request).data }
    }

    suspend fun updatePomodoro(
        pomodoroId: String,
        name: String,
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String
    ): ApiResponse<PomodoroData> {
        val request = PomodoroRequest(name, sessionsCompleted, totalSessions, theme)
        return handleApiCall { pomodoroService.updatePomodoro(pomodoroId, request).data }
    }

    suspend fun deletePomodoro(pomodoroId: String): ApiResponse<PomodoroData> {
        return handleApiCall { pomodoroService.deletePomodoro(pomodoroId).data }
    }
}
