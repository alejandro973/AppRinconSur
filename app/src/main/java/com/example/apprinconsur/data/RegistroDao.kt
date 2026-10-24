package com.example.apprinconsur.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    // Obtener todos los registros ordenados del más reciente al más antiguo
    @Query("SELECT * FROM registros_acceso ORDER BY id DESC")
    fun getAllRegistros(): Flow<List<RegistroEntity>>

    // Insertar un nuevo registro (ideal para cuando el usuario guarde el formulario)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistro(registro: RegistroEntity): Long

    // Actualizar un registro existente
    @Update
    suspend fun updateRegistro(registro: RegistroEntity)

    // Eliminar un registro
    @Delete
    suspend fun deleteRegistro(registro: RegistroEntity)

    // Buscar un registro específico por su ID
    @Query("SELECT * FROM registros_acceso WHERE id = :id LIMIT 1")
    suspend fun findRegistroById(id: Long): RegistroEntity?
}