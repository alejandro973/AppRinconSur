package com.example.apprinconsur.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    // Obtener el perfil del usuario de forma reactiva
    @Query("SELECT * FROM perfil_usuario LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    // Insertar o actualizar el perfil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: ProfileEntity)
}
