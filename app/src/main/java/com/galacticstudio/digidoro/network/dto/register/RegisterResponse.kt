package com.galacticstudio.digidoro.network.dto.register

import com.google.gson.annotations.SerializedName
import java.util.Date

data class RegisterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData
)

data class UserData(
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("date_birth") val dateOfBirth: Date,
    @SerializedName("roles") val roles: List<String>,
    @SerializedName("tokens") val tokens: List<String>,
)