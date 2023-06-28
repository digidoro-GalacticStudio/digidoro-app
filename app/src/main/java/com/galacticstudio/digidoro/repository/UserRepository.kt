package com.galacticstudio.digidoro.repository

import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationRequest
import com.galacticstudio.digidoro.network.dto.emailrecuperation.RecuperationResponse
import com.galacticstudio.digidoro.network.dto.recoverypassword.RecoveryPasswordRequest
import com.galacticstudio.digidoro.network.dto.register.UserData
import com.galacticstudio.digidoro.network.dto.user.ProfileData
import com.galacticstudio.digidoro.network.dto.user.UserRequest
import com.galacticstudio.digidoro.network.dto.user.UserResponse
import com.galacticstudio.digidoro.network.service.TodoService
import com.galacticstudio.digidoro.network.service.UserService
import com.galacticstudio.digidoro.repository.utils.handleApiCall

class UserRepository(
    private val userService: UserService
) {
    suspend fun emailRecuperation(email: String): ApiResponse<String> {
        val request = RecuperationRequest(email)
        return handleApiCall { userService.emailRecuperation(request).message.toString() }
    }

    suspend fun updateUser(
        firstname: String,
        lastname: String,
        username: String,
        dateOfBirth: String,
        phoneNumber: String
    ): ApiResponse<UserResponse> {
        val request = UserRequest(firstname, lastname, username, dateOfBirth, phoneNumber)
        return handleApiCall { userService.updateUser(request) }
    }

    suspend fun getCurrentUser(): ApiResponse<ProfileData> {
        return handleApiCall { userService.getCurrentUser().data }
    }
}