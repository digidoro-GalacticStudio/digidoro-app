package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationRequest
import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationResponse
import com.galacticstudio.digidoro.network.dto.note.NoteListResponse
import com.galacticstudio.digidoro.network.dto.note.NoteRequest
import com.galacticstudio.digidoro.network.dto.user.PremiumResponse
import com.galacticstudio.digidoro.network.dto.user.UserRequest
import com.galacticstudio.digidoro.network.dto.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("api/emailRecuperation")
    suspend fun emailRecuperation(@Body note: RecuperationRequest): RecuperationResponse

    @GET("api/user/own")
    suspend fun getCurrentUser(): UserResponse

    @PATCH("api/user/own")
    suspend fun updateUser(@Body user: UserRequest): UserResponse

    @POST("api/user/premium")
    suspend fun upgradeUser(): PremiumResponse
}