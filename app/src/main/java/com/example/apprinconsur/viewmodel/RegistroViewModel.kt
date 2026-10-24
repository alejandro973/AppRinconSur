package com.example.apprinconsur.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apprinconsur.data.AppDatabase
import com.example.apprinconsur.data.RegistroEntity
import com.example.apprinconsur.data.RegistroRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    // Inicializamos la base de datos, el DAO y el Repositorio de manera limpia
    private val database = AppDatabase.get(application)
    private val repository = RegistroRepository(database.RegistroDao())

    // Lista reactiva de registros expuesta hacia la interfaz (Jetpack Compose)
    val listaRegistros: StateFlow<List<RegistroEntity>> = repository.registros
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Función para agregar un nuevo registro desde el formulario
    fun guardarRegistro(
        nombreAcceso: String,
        tipoFlujo: String,
        movimiento: String,
        rangoEtario: String,
        cantidadPersonas: Int,
        observaciones: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.agregarRegistro(
                    nombreAcceso = nombreAcceso,
                    tipoFlujo = tipoFlujo,
                    movimiento = movimiento,
                    rangoEtario = rangoEtario,
                    cantidadPersonas = cantidadPersonas,
                    observaciones = observaciones
                )
                // Callback opcional para limpiar campos o navegar al guardar con éxito
                onSuccess()
            } catch (e: Exception) {
                // Aquí puedes manejar errores de validación si deseas mostrar un mensaje en pantalla
                e.printStackTrace()
            }
        }
    }

    // Función para eliminar un registro existente
    fun eliminarRegistro(registro: RegistroEntity) {
        viewModelScope.launch {
            repository.eliminarRegistro(registro)
        }
    }
}