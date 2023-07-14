package com.galacticstudio.digidoro.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.network.dto.folder.FolderData
import com.galacticstudio.digidoro.network.dto.note.toListNotesId
import com.google.gson.Gson

@Dao
interface FolderDao {
    //insert queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteNotes: List<FolderModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(favoriteNote: FolderModelEntity)

    //get queries
    @Query("SELECT * FROM folder")
    suspend fun getAllFolder(): List<FolderModelEntity>

    @Query("SELECT * FROM folder WHERE _id = :id")
    suspend fun getFolderById(id: String): FolderModelEntity
    @Query("SELECT * FROM folder ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastFolder(): FolderModelEntity

    //get note by id
    @Query("SELECT * FROM note WHERE _id =:id")
    suspend fun getNote(id: String) : NoteModelEntity

    //set notes by id with given notes array
    @Query("UPDATE folder SET notes_id =:notes WHERE _id =:id")
    suspend fun updateFolderNotes(id: String, notes: List<String>)
    @Query("UPDATE folder SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)

    //delete folder
    @Query("DELETE FROM folder WHERE _id =:id")
    suspend fun deleteFolder(id: String)
    @Query("DELETE FROM folder")
    suspend fun deleteFolders()

    //create one transaction
    @Transaction
    suspend fun CreateFolder(folder: FolderModelEntity): FolderModelEntity {
        insertOne(folder)
        return getLastFolder()
    }

    //toggle notes transaction
    @Transaction
    suspend fun toggleNotesInFolder(folderId: String, noteId: String): FolderModelEntity{
        val folder = getFolderById(folderId).notes_id
        val existingNote = folder.find { it._id == noteId }
        val updatedNotes = if(existingNote != null){
            folder.filterNot { it._id == noteId }
        }
        else folder.plusElement(getNote(noteId))

        val notesId = updatedNotes.map { it._id }
        updateFolderNotes(folderId, notesId)
        return getFolderById(folderId)
    }
}