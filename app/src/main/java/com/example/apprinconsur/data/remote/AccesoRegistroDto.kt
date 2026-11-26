package com.example.apprinconsur.data.remote

data class AccesoRegistroDto(
    val id: Long? = null,
    val nombreAcceso: String,
    val tipoFlujo: String,
    val movimiento: String,
    val rangoEtario: String,
    val cantidadPersonas: Int,
    val observaciones: String? = null,
    val fechaCreacion: String? = null
)