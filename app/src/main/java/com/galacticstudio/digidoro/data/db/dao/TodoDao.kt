package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import java.util.Calendar
import java.util.Date

@Dao
interface TodoDao {

    //queries to insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoList: MutableList<TodoItemModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(todo: TodoItemModelEntity)

    //select
    @Query("SELECT * FROM todoitem")
    suspend fun getAllTodo(): MutableList<TodoItemModelEntity>

    @Query("SELECT * FROM todoitem WHERE _id = :id")
    suspend fun getTodoById(id: String): TodoItemModelEntity

    @Query("SELECT * FROM todoitem ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastInsertedTodo(): TodoItemModelEntity

    //update
    @Query("UPDATE todoitem SET " +
            "title = CASE WHEN :title != '' THEN :title ELSE title END, " +
            "description = CASE WHEN :description != '' THEN :description ELSE description END, " +
            "theme = CASE WHEN :theme != '' THEN :theme ELSE theme END, " +
            "theme = CASE WHEN :theme != NULL THEN :theme ELSE theme END, " +
            "reminder = CASE WHEN :reminder != NULL THEN :reminder ELSE reminder END," +
            "updatedAt =:updatedAt " +
            " WHERE _id =:id")
    suspend fun updateTodo(
        id: String,
        title:String = "",
        description: String = "",
        theme: String = "",
        reminder: String="",
        updatedAt: Date = Calendar.getInstance().time)

    @Query("UPDATE todoitem SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)

    //  set update to completed todos
    @Query("UPDATE todoitem SET is_completed = :status WHERE _id =:id")
    suspend fun toggleStatusInTodo(id: String, status: Boolean)

    //delete
    @Query("DELETE FROM todoitem WHERE _id =:id")
    suspend fun deleteTodo(id: String)

    @Query("DELETE FROM todoitem")
    suspend fun deleteAllTodo()

    //transactions
    //insert one
    @Transaction
    suspend fun insertTodo(todo: TodoItemModelEntity): TodoItemModelEntity{
        insertOne(todo)
        return getLastInsertedTodo()
    }

    //update
    @Transaction
    suspend fun updateTodoById(id: String, title:String = "", description: String = "", theme: String = "", reminder: String=""): TodoItemModelEntity{
        updateTodo(id, title, description, theme, reminder)
        return getTodoById(id)
    }

    //toggle todo
    @Transaction
    suspend fun toggleStatusById(id: String): TodoItemModelEntity{
        val isCompleted = getTodoById(id)
        val updateStatus = !isCompleted.is_completed
        toggleStatusInTodo(id, updateStatus)
        isCompleted.is_completed = updateStatus
        return isCompleted
    }

    //delete
    @Transaction
    suspend fun deleteTodoById(id: String): TodoItemModelEntity{
        val response = getTodoById(id)
        deleteTodo(id)
        return response
    }
}