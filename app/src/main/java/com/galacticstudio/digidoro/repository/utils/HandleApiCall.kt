package com.galacticstudio.digidoro.repository.utils

import android.util.Log
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun <T> handleApiCall(call: suspend () -> T): ApiResponse<T> {
    return try {
        val response = call()
        ApiResponse.Success(response)
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        Log.d("MyErrors", "-::[ERROR]::- errorBody: ${errorBody}")
        val response = Gson().fromJson(errorBody, ErrorResponse::class.java)
        Log.d("MyErrors", "-::[ERROR]::- response: ${response}")

        val code = response?.code ?: -1
        val message = response?.message

        when (code) {
            409, 401, 404 -> {
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
        val errorMessage = when (e) {
            is UnknownHostException, is ConnectException -> "No internet connection"
            else -> "API request failed: ${e.message}"
        }
        return ApiResponse.ErrorWithMessage(errorMessage)
    } catch (e: Exception) {
        Log.d("MyErrors", "GO ERRORRR ${e}")
        return ApiResponse.Error(e)
    }
}