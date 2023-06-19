package com.galacticstudio.digidoro.repository

import android.util.Log
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.ErrorResponse
import com.galacticstudio.digidoro.network.dto.login.LoginRequest
import com.galacticstudio.digidoro.network.dto.login.LoginResponse
import com.galacticstudio.digidoro.network.dto.register.RegisterRequest
import com.galacticstudio.digidoro.network.service.AuthService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class CredentialsRepository(private val api: AuthService) {

    suspend fun login(email: String, password: String): ApiResponse<LoginResponseData> {
        try {
            val response = api.login(LoginRequest(email, password))
            val loginResponseData = LoginResponseData(
                token = response.data.token,
                username = response.data.username,
                roles = response.data.roles
            )
            return ApiResponse.Success(loginResponseData)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, LoginResponse::class.java).message

            val errorMessage = if (response is String) {
                response
            } else {
                val messageJson = response as? Map<*, *>
                messageJson?.get("error") as? String ?: "Unknown error"
            }

            return ApiResponse.ErrorWithMessage(errorMessage)
        } catch (e: IOException) {
            return ApiResponse.ErrorWithMessage("No internet connection")
        } catch (e: Exception) {
            return ApiResponse.Error(e)
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
    ): ApiResponse<String>{
        try{
            val response = api.register(
                RegisterRequest(
                    name = firstname,
                    lastname = lastname,
                    email = email,
                    username = username,
                    password = password,
                    date_birth = dateOfBirth,
                    phone_number = phoneNumber
                )
            )
            return ApiResponse.Success(response.message)

        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, ErrorResponse::class.java)

            val code = response?.code ?: -1
            val message = response?.message

            return when (code) {
                409 -> {
                    val error = if (message is String) message else (message as? Map<*, *>)?.get("error") as? String
                    ApiResponse.ErrorWithMessage(error ?: "Unknown error")
                }
                500 -> {
                    val error = if (message is String) message else (message as? Map<*, *>)?.get("error") as? String
                    val keyPattern = response?.error?.keyPattern
                    val keyValue = response?.error?.keyValue

                    if (keyPattern != null && keyValue != null) {
                        val fieldName = keyPattern.keys.firstOrNull()
                        val fieldValue = keyValue.values.firstOrNull()?.toString()
                        if (fieldName != null && fieldValue != null) {
                            val errorMessage = "The value $fieldValue is already registered as $fieldName"
                            ApiResponse.ErrorWithMessage(errorMessage)
                        } else {
                            ApiResponse.ErrorWithMessage(error ?: "Unknown error")
                        }
                    } else {
                        ApiResponse.ErrorWithMessage(error ?: "Unknown error")
                    }
                }
                400 -> {
                    val validationErrors = response?.error?.errors
                    val errorMessage = validationErrors?.firstOrNull()?.message ?: "Validation error"
                    ApiResponse.ErrorWithMessage(errorMessage)
                }
                else -> ApiResponse.ErrorWithMessage("Unknown error")
            }
        } catch (e: IOException) {
            return ApiResponse.ErrorWithMessage("No internet connection")
        } catch (e: Exception) {
            return ApiResponse.Error(e)
        }
    }

    data class LoginResponseData(
        val token: String,
        val username: String,
        val roles: List<String>
    )
}