package com.galacticstudio.digidoro.network.retrofit

import com.galacticstudio.digidoro.network.service.AuthService
import com.galacticstudio.digidoro.network.service.FavoriteNoteService
import com.galacticstudio.digidoro.network.service.FolderService
import com.galacticstudio.digidoro.network.service.NoteService
import com.galacticstudio.digidoro.network.service.RankingService
import com.galacticstudio.digidoro.network.service.TodoService
import com.galacticstudio.digidoro.network.service.UserService
import com.galacticstudio.digidoro.util.NetworkService.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var token = ""
    private var username = ""
    private var roles: List<String> = emptyList()

    private val interceptor = AuthInterceptor(token)

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    fun setToken(token: String) {
        this.token = token
        interceptor.updateToken(token)
    }

    fun clearToken() {
        interceptor.updateToken("")
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setRoles(roles: List<String>) {
        this.roles = roles
    }

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    fun getNoteService(): NoteService {
        return retrofit.create(NoteService::class.java)
    }

    fun getFavoriteNoteService(): FavoriteNoteService {
        return retrofit.create(FavoriteNoteService::class.java)
    }

    fun getFolderService(): FolderService {
        return retrofit.create(FolderService::class.java)
    }

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getRankingService(): RankingService {
        return retrofit.create(RankingService::class.java)
    }

    fun getTodoService(): TodoService{
        return retrofit.create(TodoService::class.java)
    }
}

