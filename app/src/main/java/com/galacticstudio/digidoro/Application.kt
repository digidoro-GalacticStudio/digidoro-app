package com.galacticstudio.digidoro

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.galacticstudio.digidoro.data.db.DigidoroDataBase
import com.galacticstudio.digidoro.network.retrofit.RetrofitInstance
import com.galacticstudio.digidoro.repository.CredentialsRepository
import com.galacticstudio.digidoro.repository.FavoriteNoteRepository
import com.galacticstudio.digidoro.repository.FolderRepository
import com.galacticstudio.digidoro.repository.NoteRepository
import com.galacticstudio.digidoro.repository.RankingRepository
import com.galacticstudio.digidoro.repository.TodoRepository
import com.galacticstudio.digidoro.repository.UserRepository

class Application : Application() {

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

    private fun getFavoriteNoteAPIService() = with(RetrofitInstance){
        getFavoriteNoteService()
    }

    private fun getFolderAPIService() = with(RetrofitInstance){
        getFolderService()
    }

    private fun getUserAPIService() = with(RetrofitInstance){
        getUserService()
    }

    private fun getRankingAPIService() = with(RetrofitInstance){
        getRankingService()
    }

    private fun getTodoApiService() = with(RetrofitInstance){
        getTodoService()
    }
    fun getToken(): String = prefs.getString(USER_TOKEN, "")!!

    fun getRoles(): List<String> = prefs.getStringSet(USER_ROLES, emptySet())?.toList() ?: emptyList()

    fun getUsername(): String = prefs.getString(USERNAME, "")!!

    fun hasToken(): Boolean {
        val token = getToken()
        return token.isNotEmpty()
    }

    private val database: DigidoroDataBase by lazy {
        DigidoroDataBase.getInstance(this)
    }

    //initialize repositories
    val credentialsRepository: CredentialsRepository by lazy {
        CredentialsRepository(getAPIService(), database)
    }

    val notesRepository: NoteRepository by lazy {
        NoteRepository(getNoteAPIService(), database)
    }

    val favoriteNotesRepository: FavoriteNoteRepository by lazy {
        FavoriteNoteRepository(getFavoriteNoteAPIService(), database)
    }

    val folderRepository: FolderRepository by lazy {
        FolderRepository(getFolderAPIService(), database)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(getUserAPIService(), database)
    }

    val rankingRepository: RankingRepository by lazy {
        RankingRepository(getRankingAPIService(), database)
    }

    val todoRepository: TodoRepository by lazy{
        TodoRepository(getTodoApiService(), database)
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()

        RetrofitInstance.setToken(token)
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()

        RetrofitInstance.setToken("")
        RetrofitInstance.clearToken()
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