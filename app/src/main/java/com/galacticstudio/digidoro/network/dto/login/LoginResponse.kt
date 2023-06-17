package com.galacticstudio.digidoro.network.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any
)
