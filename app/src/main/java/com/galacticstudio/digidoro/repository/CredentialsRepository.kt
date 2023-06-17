package com.galacticstudio.digidoro.repository

import android.util.Log
import com.galacticstudio.digidoro.RetrofitApplication
import com.galacticstudio.digidoro.network.ApiResponse
import com.galacticstudio.digidoro.network.dto.login.LoginRequest
import com.galacticstudio.digidoro.network.dto.login.LoginResponse
import com.galacticstudio.digidoro.network.dto.register.RegisterRequest
import com.galacticstudio.digidoro.network.service.AuthService
import com.google.gson.JsonParser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CredentialsRepository(private val api: AuthService) {

    suspend fun login(email: String, password: String): ApiResponse<String> {
        try {
            val response = api.login(LoginRequest(email, password))
            return ApiResponse.Success(response.message.toString())

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

    suspend fun register(name: String, email: String, password: String): ApiResponse<String>{
        try{
            val response = api.register(RegisterRequest(name = name, email = email, password = password))
            return ApiResponse.Success(response.message)

        } catch (e: HttpException){
            //TODO Implement this
            if(e.code() == 400){
                return ApiResponse.ErrorWithMessage("Invalid fields, name, email or password")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.ErrorWithMessage("No internet connection")
        } catch (e: Exception) {
            return ApiResponse.Error(e)
        }
    }

}
