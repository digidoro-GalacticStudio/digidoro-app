package com.galacticstudio.digidoro.network.retrofit

import okhttp3.Interceptor

class AuthInterceptor(private var token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "DigiBearer $token")
            .build()
        return chain.proceed(request)
    }

    fun updateToken(newToken: String) {
        token = newToken
    }
}