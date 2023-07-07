package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.data.db.models.PomodoroModelEntity

@Dao
interface PomodoroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noteList: List<PomodoroModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(note: PomodoroModelEntity)

    @Query("SELECT * FROM pomodoro")
    suspend fun getAllPomodoros(): List<PomodoroModelEntity>

    @Query("SELECT * FROM pomodoro WHERE _id = :id")
    suspend fun getPomodoroById(id: String): PomodoroModelEntity
}