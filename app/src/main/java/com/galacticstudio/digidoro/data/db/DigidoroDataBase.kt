package com.galacticstudio.digidoro.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.db.converters.DateConverter
import com.galacticstudio.digidoro.data.db.converters.ListStringConverter
import com.galacticstudio.digidoro.data.db.dao.FavoriteNoteDao
import com.galacticstudio.digidoro.data.db.dao.FolderDao
import com.galacticstudio.digidoro.data.db.dao.NoteDao
import com.galacticstudio.digidoro.data.db.dao.PomodoroDao
import com.galacticstudio.digidoro.data.db.dao.RankingDao
import com.galacticstudio.digidoro.data.db.dao.TodoDao
import com.galacticstudio.digidoro.data.db.dao.UserDao
import com.galacticstudio.digidoro.data.db.models.FavoriteNotesModelEntity
import com.galacticstudio.digidoro.data.db.models.FolderModelEntity
import com.galacticstudio.digidoro.data.db.models.NoteModelEntity
import com.galacticstudio.digidoro.data.db.models.PomodoroModelEntity
import com.galacticstudio.digidoro.data.db.models.TodoItemModelEntity
import com.galacticstudio.digidoro.data.db.models.UsersModelEntity

@Database(
    entities = [UsersModelEntity::class, TodoItemModelEntity::class, PomodoroModelEntity::class, NoteModelEntity::class, FolderModelEntity::class, FavoriteNotesModelEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [DateConverter::class, ListStringConverter::class]
)
abstract class DigidoroDataBase() : RoomDatabase() {
    abstract fun FavoriteNoteDao() : FavoriteNoteDao
    abstract fun FolderDao() : FolderDao
    abstract fun NoteDao() : NoteDao
    abstract fun RankingDao() : RankingDao
    abstract fun TodoDao() : TodoDao
    abstract fun UserDao() : UserDao

    abstract fun PomodoroDao() : PomodoroDao

    companion object{
        private var INSTANCE: DigidoroDataBase ?= null
        fun getInstance(application: Application) : DigidoroDataBase =
            INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    DigidoroDataBase::class.java,
                    "digidoro"
                )
                    .build()
                INSTANCE = instance
                instance
            }
    }
}