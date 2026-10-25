package com.example.apprinconsur.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil_usuario")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val correo: String,
    val rutaImagen: String = "" // Aquí guardaremos la ruta o URI de la foto que tome la cámara
)