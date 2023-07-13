package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.galacticstudio.digidoro.data.db.models.PendingRequestEntity

@Dao
interface PendingRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingRequest(request: PendingRequestEntity)

//    @Update
//    suspend fun updatePendingRequest(request: PendingRequestEntity)

    @Query("DELETE FROM pending WHERE _id=:id")
    suspend fun deletePendingRequest(id: String)

    @Query("SELECT * FROM pending")
    suspend fun getAllPendingRequests(): List<PendingRequestEntity>
}
