package com.example.apprinconsur.model

import java.util.UUID

// Representa un registro individual de flujo de clientes en la tienda
data class RegistroItem(
    val id: String = UUID.randomUUID().toString(), // Identificador único para cada registro
    val nombreAcceso: String,                      // Acceso de la tienda (ej: "Acceso 1")
    val tipoFlujo: String,                         // "Tránsito" o "Compra"
    val rangoEtario: String,                       // "Joven", "Adulto", "Adulto Mayor"
    val cantidadPersonas: Int,                     // Cantidad numérica ingresada
    val observaciones: String                      // Comentarios opcionales
)