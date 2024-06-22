package com.example.bismillahbisaintermediate.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bismillahbisaintermediate.Response.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class ListStoryDB : RoomDatabase() {
    abstract fun storyDao(): ListStoryDao
    companion object {
        @Volatile
        private var INSTANCE: ListStoryDB? = null

        @JvmStatic
        fun getDatabase(context: Context): ListStoryDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ListStoryDB::class.java, "ListStoryDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}