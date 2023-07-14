package com.galacticstudio.digidoro.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.network.dto.favoritenote.FavoriteNoteDao
import com.galacticstudio.digidoro.network.dto.note.NoteData
import com.galacticstudio.digidoro.network.dto.note.toListNoteData
import com.galacticstudio.digidoro.network.dto.note.toListNotesId
import com.google.gson.Gson

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
    suspend fun updateFavoriteNotesNoteById(id: String, notes: List<NoteModelEntity>)

    //get note by id
    @Query("SELECT * FROM note WHERE _id =:id")
    suspend fun getNote(id: String) : NoteModelEntity


//  set better update to toggle elements in array notes id
    @Transaction
    suspend fun toggleFavoriteNoteById(noteId: String): List<String>{
        val favoriteFolder = getFavoriteNoteWithAllNotes()[0]
        val favoriteNotes = favoriteFolder.notes_id
        val favoriteFolderId = favoriteFolder._id
        val existingNote = favoriteNotes.find { it._id == noteId }
        val updatedNotes = if(existingNote != null){
            favoriteNotes.filterNot { it._id == noteId }
        }
        else favoriteNotes.plusElement(getNote(noteId))
        updateFavoriteNotesNoteById(favoriteFolderId, updatedNotes)
        Log.d("notes_id", "notes: " + Gson().toJson(updatedNotes))
        return getFavoriteNoteWithAllNotes()[0].notes_id.toListNotesId()
    }

    @Query("DELETE FROM favoritenotes")
    suspend fun deleteFavoriteNotes()

}