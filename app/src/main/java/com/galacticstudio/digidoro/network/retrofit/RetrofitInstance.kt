package com.galacticstudio.digidoro.network.retrofit

import com.galacticstudio.digidoro.network.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://digidoroapi.onrender.com/"

    object RetrofitInstance {

        private var token = ""

        fun setToken(token: String) {
            this.token = token
        }

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getLoginService(): AuthService {
            return retrofit.create(AuthService::class.java)
        }
    }
