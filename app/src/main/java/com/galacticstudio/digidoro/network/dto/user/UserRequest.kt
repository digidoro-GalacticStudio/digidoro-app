package com.galacticstudio.digidoro.network.dto.user

data class UserRequest(
    val firstname: String,
    val lastname: String,
    val username: String,
    val date_birth: String,
    val phone_number: String
)
