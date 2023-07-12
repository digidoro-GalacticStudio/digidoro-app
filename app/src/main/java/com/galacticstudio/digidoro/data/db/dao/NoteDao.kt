package com.galacticstudio.digidoro.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity

@Dao
interface NoteDao {

    //insert queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noteList: List<NoteModelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(note: NoteModelEntity)

    //select queries
    @Query("SELECT * FROM note where is_trashed =:is_trash")
    suspend fun getAllNote(is_trash: Boolean): List<NoteModelEntity>

    @Query("SELECT * FROM note WHERE _id = :id")
    suspend fun getNote(id: String): NoteModelEntity

    @Query("SELECT * FROM note ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastInsertedNote(): NoteModelEntity


    //update queries
    @Query("UPDATE note SET " +
            "title = CASE WHEN :title != '' THEN :title ELSE title END, " +
            "message = CASE WHEN :message != '' THEN :message ELSE message END, " +
            "theme = CASE WHEN :theme != '' THEN :theme ELSE theme END, " +
            "tags = CASE WHEN :tags != NULL THEN :tags ELSE tags end, " +
            "is_trashed = CASE WHEN :is_trash != NULL THEN :is_trash ELSE is_trashed " +
            "END WHERE _id =:id")
    suspend fun update(id: String, title:String = "", message: String = "", theme: String = "", tags: List<String>? , is_trash: Boolean?)

    //update tags note
    @Query("UPDATE note SET tags =:tags WHERE _id =:id")
    suspend fun updateTagsInNote(id: String, tags: List<String>)

    //update note theme
    @Query("UPDATE note SET theme =:theme WHERE _id =:id")
    suspend fun updateThemeById(id: String, theme:String)

    //  update note trash
    @Query("UPDATE note SET is_trashed = :trash WHERE _id =:id")
    suspend fun updateTrashInNote(id: String, trash: Boolean)


    //delete Note
    @Query("DELETE FROM note")
    suspend fun deleteNotes()

    @Query("DELETE FROM note WHERE _id =:id")
    suspend fun deleteOne(id: String)


    //transaction create
    @Transaction
    suspend fun createNote(note: NoteModelEntity): NoteModelEntity{
        insertOne(note)
        return getLastInsertedNote()
    }

    //transaction update
    @Transaction
    suspend fun updateNoteById(id: String, note: NoteModelEntity): NoteModelEntity{
        update(id = id, title = note.title, message = note.message, theme = note.theme, tags = note.tags, is_trash = note.is_trashed)
        return getNote(id)
    }
    //transaction toggle tags
    @Transaction
    suspend fun toggleTagsById(id: String, tag: String): NoteModelEntity{
        val tags = getNote(id).tags
        val updateTags = if(tags.contains(tag))
            tags.filterNot { it == tag }
        else
            tags + tag
        updateTagsInNote(id, updateTags)

        return getNote(id)
    }
    //transaction toggle trash
    @Transaction
    suspend fun toggleTrashById(id: String): NoteModelEntity{
        val isTrashed = getNote(id).is_trashed
        val updateTrash = !isTrashed

        updateTrashInNote(id, updateTrash)

        return getNote(id)
    }

    //transaction toggle theme
    @Transaction
    suspend fun toggleThemeById(id: String, theme: String): NoteModelEntity{
        updateThemeById(id, theme)
        return getNote(id)
    }
    //transaction delete one
    @Transaction
    suspend fun deleteNoteById(id: String): NoteModelEntity{
        val response = getNote(id)
        deleteOne(id)
        return response
    }

}