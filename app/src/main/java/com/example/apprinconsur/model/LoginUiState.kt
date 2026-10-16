package com.example.apprinconsur.model

// Modelo principal que almacena el estado actual del formulario de inicio de sesión
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores()
)

// Modelo para almacenar los mensajes de error específicos de cada campo
data class LoginErrores(
    val correo: String? = null,
    val clave: String? = null
)