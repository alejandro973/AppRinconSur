package com.example.apprinconsur.model

data class ProfileUiState(
    val nombre: String = "",
    val correo: String = "",
    val rutaImagen: String = "",
    val errores: ProfileErrores = ProfileErrores()
)

data class ProfileErrores(
    val nombre: String? = null,
    val correo: String? = null
)