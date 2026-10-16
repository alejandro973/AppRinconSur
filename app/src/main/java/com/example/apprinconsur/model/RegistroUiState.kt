package com.example.apprinconsur.model

data class RegistroUiState(
    val tipoFlujo: String = "",         // "Tránsito" o "Compra"
    val rangoEtario: String = "",       // "Joven", "Adulto", "Adulto Mayor"
    val cantidadPersonas: String = "1", // Cantidad ingresada
    val observaciones: String = "",     // Comentarios opcionales
    val errores: RegistroErrores = RegistroErrores()
)

// Errores específicos para cada campo del formulario
data class RegistroErrores(
    val tipoFlujo: String? = null,
    val rangoEtario: String? = null,
    val cantidadPersonas: String? = null
)