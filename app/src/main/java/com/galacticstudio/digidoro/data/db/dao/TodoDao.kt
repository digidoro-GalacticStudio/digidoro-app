package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.galacticstudio.digidoro.data.db.room.TodoItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoitem")
    fun getAllTodos(): Flow<List<TodoItemModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItemModel)

    @Update
    suspend fun updateTodo(todo: TodoItemModel)

    @Delete
    suspend fun deleteTodo(todo: TodoItemModel)
}
