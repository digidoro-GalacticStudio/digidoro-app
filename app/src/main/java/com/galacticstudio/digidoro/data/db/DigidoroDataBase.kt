package com.galacticstudio.digidoro.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.galacticstudio.digidoro.Application
import com.galacticstudio.digidoro.data.db.room.FavoriteNotesModel
import com.galacticstudio.digidoro.data.db.room.FolderModel
import com.galacticstudio.digidoro.data.db.room.NoteModel
import com.galacticstudio.digidoro.data.db.room.PomodoroModel
import com.galacticstudio.digidoro.data.db.room.TodoItemModel
import com.galacticstudio.digidoro.data.db.room.UsersModel

@Database(
    entities = [UsersModel::class, TodoItemModel::class, PomodoroModel::class, NoteModel::class, FolderModel::class, FavoriteNotesModel::class],
    version = 1,
    exportSchema = false
)
abstract class DigidoroDataBase : RoomDatabase() {

    companion object{
        private var INSTANCE: DigidoroDataBase ?= null
        fun getInstance(application: Application) : DigidoroDataBase =
            INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    DigidoroDataBase::class.java,
                    "digidoro_app"
                )
                    .build()
                INSTANCE = instance
                instance
            }
    }
}