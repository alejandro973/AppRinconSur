package com.example.apprinconsur.model

data class RegistroUiState(
    val movimiento: String = "",        // "Ingreso" o "Salida"
    val tipoFlujo: String = "",         // "Tránsito" o "Compra"
    val rangoEtario: String = "",       // "Joven", "Adulto", "Adulto Mayor"
    val cantidadPersonas: Int = 1,      // Cantidad controlada con botones - y +
    val observaciones: String = "",     // Comentarios opcionales
    val errores: RegistroErrores = RegistroErrores()
)

// Errores específicos para cada campo del formulario
data class RegistroErrores(
    val movimiento: String? = null,
    val tipoFlujo: String? = null,
    val rangoEtario: String? = null
)