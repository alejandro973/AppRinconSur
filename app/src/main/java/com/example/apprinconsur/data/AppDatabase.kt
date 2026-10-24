package com.example.apprinconsur.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apprinconsur.data.RegistroEntity
import com.example.apprinconsur.data.RegistroDao

@Database(
    entities = [RegistroEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RegistroDao(): RegistroDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rincon_sur.db"
                ).build().also { INSTANCE = it }
            }
    }
}
