package com.example.apprinconsur.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apprinconsur.data.RegistroEntity
import com.example.apprinconsur.data.RegistroDao
import com.example.apprinconsur.data.ProfileEntity
import com.example.apprinconsur.data.ProfileDao

@Database(
    entities = [RegistroEntity::class, ProfileEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RegistroDao(): RegistroDao
    abstract fun profileDao(): ProfileDao // <-- Agregado para el perfil del usuario

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rincon_sur.db"
                )
                    .fallbackToDestructiveMigration() // Borra y recrea la BD limpiamente si cambia la versión (ideal para desarrollo)
                    .build().also { INSTANCE = it }
            }
    }
}