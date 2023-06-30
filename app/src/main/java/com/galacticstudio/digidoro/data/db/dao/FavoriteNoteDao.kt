package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galacticstudio.digidoro.data.db.room.FavoriteNotesModel

@Dao
interface FavoriteNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteNotes: List<FavoriteNotesModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(favoriteNote: FavoriteNotesModel)

    @Query("SELECT * FROM favoritenotes")
    fun pagingSource(): List<FavoriteNotesModel>

    // Error here
//    @Query("UPDATE favoritenotes SET  = :note WHERE id = :id")
//    suspend fun updateFavoriteNoteById(id: String, note: String)

}