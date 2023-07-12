package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.data.db.models.PomodoroModelEntity
import com.galacticstudio.digidoro.ui.screens.pomodoro.PomodoroUIEvent
import java.util.Calendar
import java.util.Date

@Dao
interface PomodoroDao {
    //Insert queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pomodoroList: List<PomodoroModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(pomodoro: PomodoroModelEntity)

    //get queries
    @Query("SELECT * FROM pomodoro")
    suspend fun getAllPomodoros(): List<PomodoroModelEntity>

    @Query("SELECT * FROM pomodoro WHERE _id = :id")
    suspend fun getPomodoroById(id: String): PomodoroModelEntity

    @Query("SELECT * FROM pomodoro ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastInsertedPomodoro(): PomodoroModelEntity

    //update queries
    @Query("UPDATE pomodoro SET " +
            "name = CASE WHEN :name != '' THEN :name ELSE name END, " +
            "sessions_completed = CASE WHEN :sessionsCompleted != null THEN :sessionsCompleted ELSE sessions_completed END, " +
            "total_sessions = CASE WHEN :totalSessions != NULL THEN :totalSessions ELSE total_sessions END, " +
            "theme = CASE WHEN :theme != '' THEN :theme ELSE theme END, " +
            "updatedAt =:updatedAt " +
            " WHERE _id =:id")
    suspend fun update(
        id: String,
        name:String = "",
        sessionsCompleted: Int,
        totalSessions: Int,
        theme: String = "",
        updatedAt: Date = Calendar.getInstance().time)

    //delete queries
    @Query("DELETE FROM POMODORO")
    suspend fun deletePomodoros()

    //transactions insert
    @Transaction
    suspend fun insertPomodoro(pomodoro: PomodoroModelEntity): PomodoroModelEntity{
        insertOne(pomodoro)
        return getLastInsertedPomodoro();
    }

    //transaction to update
    @Transaction
    suspend fun updatePomodoroById(id: String, pomodoro: PomodoroModelEntity) : PomodoroModelEntity{
        update(id, pomodoro.name, pomodoro.sessions_completed, pomodoro.total_sessions, pomodoro.theme)
        return getPomodoroById(id)
    }

}