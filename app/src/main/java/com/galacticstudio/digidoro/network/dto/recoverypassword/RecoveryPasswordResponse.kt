package com.galacticstudio.digidoro.network.dto.recoverypassword

import com.galacticstudio.digidoro.network.dto.register.UserData
import com.google.gson.annotations.SerializedName

data class RecoveryPasswordResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData
)