package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noteList: List<NoteModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(note: NoteModelEntity)

    @Query("SELECT * FROM note where is_trashed =:is_trash")
    suspend fun getAllNote(is_trash: Boolean): List<NoteModelEntity>

    @Query("SELECT * FROM note WHERE _id = :id")
    suspend fun getNoteById(id: String): NoteModelEntity

    //set notes by id with given notes array
    @Query("UPDATE note SET " +
            "title = CASE WHEN :title != '' THEN :title ELSE title END, " +
            "message = CASE WHEN :message != '' THEN :message ELSE message END, " +
            "theme = CASE WHEN :theme != '' THEN :theme ELSE theme END, " +
            "tags = CASE WHEN :tags != NULL THEN :tags ELSE tags end, " +
            "is_trashed = CASE WHEN :is_trash != NULL THEN :is_trash ELSE is_trashed " +
            "END WHERE _id =:id")
    suspend fun updateNote(id: String, title:String = "", message: String = "", theme: String = "", tags: List<String>? , is_trash: Boolean?)

    //set notes by id with given notes array
    @Query("UPDATE note SET tags =:tags WHERE _id =:id")
    suspend fun updateTagsInNote(id: String, tags: List<String>)
    @Transaction
    suspend fun toggleTags(id: String, tag: String){
        val tags = getNoteById(id).tags
        val updateTags = if(tags.contains(tag))
            tags.filterNot { it == tag }
        else
            tags + tag
        updateTagsInNote(id, updateTags)
    }

    //update theme
    @Query("UPDATE note SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)
    //  set better update to trash in note
    @Query("UPDATE note SET is_trashed = :trash WHERE _id =:id")
    suspend fun toggleTrashInNote(id: String, trash: Boolean)

    @Transaction
    suspend fun toggleTrashById(id: String){
        val isTrashed = getNoteById(id).is_trashed
        val updateTrash = !isTrashed

        toggleTrashInNote(id, updateTrash)
    }

    //delete Note
    @Query("DELETE FROM note WHERE _id =:id")
    suspend fun deleteNote(id: String)

}