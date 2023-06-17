package com.galacticstudio.digidoro.network.service

import com.galacticstudio.digidoro.network.dto.login.LoginRequest
import com.galacticstudio.digidoro.network.dto.login.LoginResponse
import com.galacticstudio.digidoro.network.dto.register.RegisterRequest
import com.galacticstudio.digidoro.network.dto.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/user/singin")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @POST("api/user/register")
    suspend fun register(@Body credentials: RegisterRequest): RegisterResponse
}