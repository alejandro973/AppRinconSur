package com.example.apprinconsur.data

import kotlinx.coroutines.flow.Flow

class RegistroRepository(private val dao: RegistroDao) {

    // Obtener todos los registros como un Flow reactivo
    val registros: Flow<List<RegistroEntity>> = dao.getAllRegistros()

    // Insertar un nuevo registro de acceso con validaciones básicas
    suspend fun agregarRegistro(
        nombreAcceso: String,
        tipoFlujo: String,
        movimiento: String,
        rangoEtario: String,
        cantidadPersonas: Int,
        observaciones: String
    ) {
        require(nombreAcceso.isNotBlank()) { "El nombre del acceso no puede estar vacío" }
        require(tipoFlujo.isNotBlank()) { "El tipo de flujo no puede estar vacío" }
        require(cantidadPersonas > 0) { "La cantidad de personas debe ser mayor a cero" }

        val nuevoRegistro = RegistroEntity(
            nombreAcceso = nombreAcceso.trim(),
            tipoFlujo = tipoFlujo,
            movimiento = movimiento,
            rangoEtario = rangoEtario,
            cantidadPersonas = cantidadPersonas,
            observaciones = observaciones.trim()
        )
        dao.insertRegistro(nuevoRegistro)
    }

    // Actualizar un registro existente
    suspend fun actualizarRegistro(registro: RegistroEntity) {
        require(registro.id > 0) { "Id de registro inválido" }
        require(registro.cantidadPersonas > 0) { "La cantidad de personas debe ser mayor a cero" }
        dao.updateRegistro(registro)
    }

    // Eliminar un registro
    suspend fun eliminarRegistro(registro: RegistroEntity) = dao.deleteRegistro(registro)

    // Buscar un registro por su ID
    suspend fun obtenerRegistroPorId(id: Long) = dao.findRegistroById(id)
}