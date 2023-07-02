package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.api.TodoModel
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoList: MutableList<TodoItemModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(todo: TodoItemModelEntity)

    //by user
    @Query("SELECT * FROM todoitem WHERE user_id =:id")
    suspend fun getAllNote(id: String): List<TodoItemModelEntity>

    @Query("SELECT * FROM todoitem WHERE _id = :id")
    suspend fun getTodoById(id: String): TodoItemModelEntity

    //set notes by id with given notes array
    @Query("UPDATE todoitem SET " +
            "title = CASE WHEN :title != '' THEN :title ELSE title END, " +
            "description = CASE WHEN :description != '' THEN :description ELSE description END, " +
            "theme = CASE WHEN :theme != '' THEN :theme ELSE theme END, " +
            "theme = CASE WHEN :theme != NULL THEN :theme ELSE theme end, " +
            "reminder = CASE WHEN :reminder != NULL THEN :reminder ELSE reminder " +
            "END WHERE _id =:id")
    suspend fun updateTodoItemById(id: String, title:String = "", description: String = "", theme: String = "", reminder: String="")

    //update theme
    @Query("UPDATE todoitem SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)

    //  set better update to trash in note
    @Query("UPDATE todoitem SET is_completed = :status WHERE _id =:id")
    suspend fun toggleStatusInTodo(id: String, status: Boolean)

    @Transaction()
    suspend fun toggleStatusById(id: String){
        val is_completed = getTodoById(id).is_completed
        val updateStatus = !is_completed

        toggleStatusInTodo(id, updateStatus)
    }

    //delete Note
    @Query("DELETE FROM todoitem WHERE _id =:id")
    suspend fun deleteNote(id: String)

}