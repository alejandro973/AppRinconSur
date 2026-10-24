package com.example.apprinconsur.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros_acceso")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombreAcceso: String,
    val tipoFlujo: String,
    val movimiento: String,
    val rangoEtario: String,
    val cantidadPersonas: Int,
    val observaciones: String,
    val fechaHora: Long = System.currentTimeMillis()
)