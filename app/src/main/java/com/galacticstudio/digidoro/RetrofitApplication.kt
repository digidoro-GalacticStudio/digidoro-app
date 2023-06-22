package com.galacticstudio.digidoro

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.galacticstudio.digidoro.network.retrofit.RetrofitInstance
import com.galacticstudio.digidoro.repository.CredentialsRepository
import com.galacticstudio.digidoro.repository.NoteRepository

class RetrofitApplication : Application() {

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("Retrofit", Context.MODE_PRIVATE)
    }

    private fun getAPIService() = with(RetrofitInstance){
        setToken(getToken())
        setRoles(getRoles())
        setUsername(getUsername())
        getLoginService()
    }

    private fun getNoteAPIService() = with(RetrofitInstance){
        getNoteService()
    }

    fun getToken(): String = prefs.getString(USER_TOKEN, "")!!

    fun getRoles(): List<String> = prefs.getStringSet(USER_ROLES, emptySet())?.toList() ?: emptyList()

    fun getUsername(): String = prefs.getString(USERNAME, "")!!

    val credentialsRepository: CredentialsRepository by lazy {
        CredentialsRepository(getAPIService())
    }

    val notesRepository: NoteRepository by lazy {
        NoteRepository(getNoteAPIService())
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()

        RetrofitInstance.setToken(token)
    }

    fun saveRoles(roles: List<String>) {
        val editor = prefs.edit()
        editor.putStringSet(USER_ROLES, roles.toSet())
        editor.apply()
    }

    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ROLES = "user_roles"
        const val USERNAME = "username"
    }
}