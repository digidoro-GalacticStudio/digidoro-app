package com.galacticstudio.digidoro.network.dto.recoverypassword

data class RecoveryPasswordRequest(
    val email: String,
    val recoveryCode: Int,
    val newPassword: String
)