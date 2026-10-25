package com.example.apprinconsur.data

import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val dao: ProfileDao) {

    val profileFlow: Flow<ProfileEntity?> = dao.getProfile()

    suspend fun guardarPerfil(nombre: String, correo: String, rutaImagen: String) {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(correo.isNotBlank()) { "El correo no puede estar vacío" }

        val perfil = ProfileEntity(
            nombre = nombre.trim(),
            correo = correo.trim(),
            rutaImagen = rutaImagen
        )
        dao.insertOrUpdateProfile(perfil)
    }
}