package com.galacticstudio.digidoro.repository

import android.content.Context
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroData
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroRequest
import com.galacticstudio.digidoro.network.dto.pomodoro.toListPomdoroModelEntity
import com.galacticstudio.digidoro.network.dto.pomodoro.toListPomodoroData
import com.galacticstudio.digidoro.network.service.PomodoroService
import com.galacticstudio.digidoro.repository.utils.CheckInternetConnectivity
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class PomodoroRepository(
    private val pomodoroService: PomodoroService,
    private val database: DigidoroDataBase,
    private val context: Context
    ) {

    val pomodoroDao = database.PomodoroDao()
    suspend fun getAllPomodoros(
        sortBy: String? = null,
        order: String? = null,
        page: Int? = null,
        limit: Int? = null,
        populate: String? = null,
    ): ApiResponse<List<PomodoroData>> {
        return handleApiCall {
            val response = if(CheckInternetConnectivity(context)){
                val apiResponse = pomodoroService.getAllPomodoros(
                    sortBy,
                    order,
                    page,
                    limit,
                    populate
                ).data
                pomodoroDao.insertAll(apiResponse.toListPomdoroModelEntity())
                apiResponse
            }
            else pomodoroDao.getAllPomodoros().toListPomodoroData()

            response
        }
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

    suspend fun incrementSessions(
        pomodoroId: String,
    ): ApiResponse<String> {
        return handleApiCall { pomodoroService.incrementSessions(pomodoroId).message }
    }

    suspend fun deletePomodoro(pomodoroId: String): ApiResponse<PomodoroData> {
        return handleApiCall { pomodoroService.deletePomodoro(pomodoroId).data }
    }
}
