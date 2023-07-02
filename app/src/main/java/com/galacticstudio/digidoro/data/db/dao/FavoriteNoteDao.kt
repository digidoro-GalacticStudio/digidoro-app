package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteDao

@Dao
interface FavoriteNoteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteNotes: List<FavoriteNotesModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(favoriteNote: FavoriteNotesModelEntity)

    @Query("SELECT * FROM favoritenotes WHERE user_id =:id")
    suspend fun pagingSource(id: String): List<FavoriteNoteDao>

    @Query("SELECT * FROM favoritenotes WHERE _id = :id")
    suspend fun getFavoriteNoteById(id: String): FavoriteNotesModelEntity

    //set notes by id with given notes array
    @Query("UPDATE favoritenotes SET notes_id =:notes WHERE _id =:id")
    suspend fun updateFavoriteNotesNoteById(id: String, notes: List<String>)

//  set better update to toggle elements in array notes id
    @Transaction
    suspend fun toggleFavoriteNoteById(id: String){
        val favoriteNote = getFavoriteNoteById(id).notes_id
    val updatedFavoriteNotes = if (favoriteNote.contains(id))
            favoriteNote.filterNot { it == id }
        else
            favoriteNote + id

    updateFavoriteNotesNoteById(id, favoriteNote)
    }

    //  set better update to delete elements in array notes id
    @Transaction
    suspend fun deleteNotesById(id: String){
        val folder = getFavoriteNoteById(id).notes_id
        val updatedFolder = if (folder.contains(id))
            folder.filterNot { it == id }
        else folder

        updateFavoriteNotesNoteById(id, folder)
    }

}