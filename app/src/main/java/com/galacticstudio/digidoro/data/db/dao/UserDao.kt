package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galacticstudio.digidoro.data.db.models.UsersModelEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userList: List<UsersModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(user: UsersModelEntity)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UsersModelEntity>

    @Query("SELECT * FROM users WHERE _id = :id")
    suspend fun getTodoById(id: String): UsersModelEntity

    //set notes by id with given notes array
    @Query("UPDATE users SET " +
            "firstname = CASE WHEN :firstname != '' THEN :firstname ELSE firstname END, " +
            "lastname = CASE WHEN :lastname != '' THEN :lastname ELSE lastname END, " +
            "username = CASE WHEN :username != '' THEN :username ELSE username END, " +
            "date_birth = CASE WHEN :date_birth != NULL THEN :date_birth ELSE date_birth end, " +
            "phone_number = CASE WHEN :phone_number != NULL THEN :phone_number ELSE phone_number " +
            "END WHERE _id =:id")
    suspend fun updateTodoItemById(id: String, firstname:String = "", lastname: String = "", username: String = "", date_birth: String="", phone_number: String)


    @Query("DELETE FROM USERS")
    suspend fun deleteUsers()

}