package com.galacticstudio.digidoro.network.dto.emailrecuperation

import com.google.gson.annotations.SerializedName

data class RecuperationResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: Any,
)
