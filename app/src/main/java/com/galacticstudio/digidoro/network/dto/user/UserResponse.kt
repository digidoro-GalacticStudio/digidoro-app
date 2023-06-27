package com.galacticstudio.digidoro.network.dto.user

import com.galacticstudio.digidoro.network.dto.register.UserData
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData
)