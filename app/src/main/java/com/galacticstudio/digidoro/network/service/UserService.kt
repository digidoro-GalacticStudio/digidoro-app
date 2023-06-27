package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationRequest
import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("api/emailRecuperation")
    suspend fun emailRecuperation(@Body note: RecuperationRequest): RecuperationResponse
}