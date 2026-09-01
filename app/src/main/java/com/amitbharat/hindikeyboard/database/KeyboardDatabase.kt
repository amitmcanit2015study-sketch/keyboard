package com.amitbharat.hindikeyboard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserWordEntity::class, ClipboardEntity::class], version = 1, exportSchema = false)
abstract class KeyboardDatabase : RoomDatabase() {

    abstract fun userWordDao(): UserWordDao
    abstract fun clipboardDao(): ClipboardDao

    companion object {
        @Volatile
        private var INSTANCE: KeyboardDatabase? = null

        fun getDatabase(context: Context): KeyboardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KeyboardDatabase::class.java,
                    "indic_keyboard_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
