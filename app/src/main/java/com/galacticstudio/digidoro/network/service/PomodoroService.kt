package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroListResponse
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroRequest
import com.galacticstudio.digidoro.network.dto.pomodoro.PomodoroResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PomodoroService {
    @GET("api/pomodoro/own")
    suspend fun getAllPomodoros(
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("populateFields") populateFields: String? = null,
    ): PomodoroListResponse

    @GET("api/pomodoro/own/{id}")
    suspend fun getPomodoroById(@Path("id") pomodoroId: String): PomodoroResponse

    @POST("api/pomodoro/own")
    suspend fun createPomodoro(@Body pomodoro: PomodoroRequest): PomodoroResponse

    @PATCH("api/pomodoro/own/{id}")
    suspend fun updatePomodoro(@Path("id") pomodoroId: String, @Body pomodoro: PomodoroRequest): PomodoroResponse

    @DELETE("api/pomodoro/own/{id}")
    suspend fun deletePomodoro(@Path("id") pomodoroId: String): PomodoroResponse
}
