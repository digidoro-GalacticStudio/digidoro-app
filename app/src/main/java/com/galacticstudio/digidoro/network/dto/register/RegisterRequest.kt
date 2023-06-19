package com.galacticstudio.digidoro.network.dto.register

data class RegisterRequest(
    val name: String,
    val lastname: String,
    val email: String,
    val username: String,
    val password: String,
    val date_birth: String,
    val phone_number: String
)