package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.recoverypassword.RecoveryPasswordRequest
import com.galacticstudio.digidoro.network.dto.login.LoginRequest
import com.galacticstudio.digidoro.network.dto.register.RegisterRequest
import com.galacticstudio.digidoro.network.dto.register.UserData
import com.galacticstudio.digidoro.network.service.AuthService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class CredentialsRepository(
    private val api: AuthService,
    private val database: DigidoroDataBase
    ) {
    private val userDao = database.UserDao()
    suspend fun login(email: String, password: String): ApiResponse<LoginResponseData> {
        return handleApiCall {
            val response = api.login(LoginRequest(email, password))
            val loginResponseData = LoginResponseData(
                token = response.data.token,
                username = response.data.username,
                roles = response.data.roles
            )
            loginResponseData
        }
    }

    suspend fun register(
        firstname: String,
        lastname: String,
        email: String,
        username: String,
        password: String,
        dateOfBirth: String,
        phoneNumber: String,
    ): ApiResponse<String> {
        val request = RegisterRequest(
            firstname = firstname,
            lastname = lastname,
            email = email,
            username = username,
            password = password,
            date_birth = dateOfBirth,
            phone_number = phoneNumber
        )
        return handleApiCall { api.register(request).message }
    }

    suspend fun recoveryCredentials(email: String, recoveryCode: Int, newPassword: String): ApiResponse<UserData> {
        val request = RecoveryPasswordRequest(email,recoveryCode, newPassword)
        return handleApiCall { api.recoveryCredentials(request).data }
    }

    data class LoginResponseData(
        val token: String,
        val username: String,
        val roles: List<String>
    )
}