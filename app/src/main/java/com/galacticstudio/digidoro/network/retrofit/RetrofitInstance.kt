package com.galacticstudio.digidoro.network.retrofit

import com.galacticstudio.digidoro.network.service.AuthService
import com.galacticstudio.digidoro.util.NetworkService.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var token = ""
    private var username = ""
    private var roles: List<String> = emptyList()

    fun setToken(token: String) {
        this.token = token
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setRoles(roles: List<String>) {
        this.roles = roles
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}
