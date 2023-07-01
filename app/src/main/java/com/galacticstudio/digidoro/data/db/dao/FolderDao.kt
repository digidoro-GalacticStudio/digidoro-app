package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.network.dto.folder.FolderData

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteNotes: List<FolderModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(favoriteNote: FolderModelEntity)

    @Query("SELECT * FROM folder")
    suspend fun getAllFolder(): List<FolderModelEntity>

    @Query("SELECT * FROM folder WHERE _id = :id")
    suspend fun getFolderById(id: String): FolderModelEntity

    //set notes by id with given notes array
    @Query("UPDATE folder SET notes_id =:notes WHERE _id =:id")
    suspend fun updateFolderNoteById(id: String, notes: List<String>)

    //  set better update to toggle elements in array notes id
    @Transaction
    suspend fun toggleNotesById(id: String){
        val folder = getFolderById(id).notes_id
        val updatedFolder = if (folder.contains(id))
            folder.filterNot { it == id }
        else folder + id

        updateFolderNoteById(id, folder)
    }
    //update theme
    @Query("UPDATE folder SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)

    //  set better update to delete elements in array notes id
    @Transaction
    suspend fun deleteNotesById(id: String){
        val folder = getFolderById(id).notes_id;
        val updatedFolder = if (folder.contains(id))
            folder.filterNot { it == id }
        else folder

        updateFolderNoteById(id, folder)
    }

    //delete folder
    @Query("DELETE FROM folder WHERE _id =:id")
    suspend fun deleteFolder(id: String)
}