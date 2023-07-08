package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteDao
import com.galacticstudio.digidoro.network.dto.note.NoteData

@Dao
interface FavoriteNoteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteNotes: List<FavoriteNotesModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(favoriteNote: FavoriteNotesModelEntity)

    @Query("SELECT * FROM favoritenotes")
    suspend fun getFavoriteNoteWithAllNotes(): List<FavoriteNotesModelEntity>

    @Query("SELECT * FROM favoritenotes WHERE _id = :id")
    suspend fun getFavoriteNoteById(id: String): FavoriteNotesModelEntity

    //set notes by id with given notes array
    @Query("UPDATE favoritenotes SET notes_id =:notes WHERE _id =:id")
    suspend fun updateFavoriteNotesNoteById(id: String, notes: List<String>)

    //get note by id
    @Query("SELECT * FROM note WHERE _id =:id")
    suspend fun getNoteInFavorite(id: String) : NoteModelEntity


//  set better update to toggle elements in array notes id
//    @Transaction
//    suspend fun toggleFavoriteNoteById(id: String){
//    val folder = getFavoriteNoteById(id).notes_id
//    val updatedFolder = folder.map{ element ->
//        if(element._id == id) folder.filterNot { it == element }
//        else folder + getNoteInFavorite(id)
//    }
//        updateFavoriteNotesNoteById(id, updatedFolder)
//    }

    //  set better update to delete elements in array notes id
//    @Transaction
//    suspend fun deleteNotesById(id: String){
//        val folder = getFavoriteNoteById(id).notes_id
//        val updatedFolder = if (folder.contains(id))
//            folder.filterNot { it == id }
//        else folder
//
//        updateFavoriteNotesNoteById(id, folder)
//    }

    @Query("DELETE FROM favoritenotes")
    suspend fun deleteFavoriteNotes()

}