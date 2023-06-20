package com.galacticstudio.digidoro.network.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any,
    @SerializedName("data") val data: UserData,
    @SerializedName("error") val error: String,
)
data class UserData(
    @SerializedName("token") val token: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("roles") val roles: List<String>,
)