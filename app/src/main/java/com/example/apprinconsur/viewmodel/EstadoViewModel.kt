package com.example.apprinconsur.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apprinconsur.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    // Progreso de la barra de carga (de 0.0 a 1.0)
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso

    // Indica si la carga ha finalizado para disparar la navegación automática
    private val _cargaFinalizada = MutableStateFlow(false)
    val cargaFinalizada: StateFlow<Boolean> = _cargaFinalizada

    init {
        iniciarCarga()
    }

    private fun iniciarCarga() {
        viewModelScope.launch {
            // Simulamos la lectura de datos y el avance de la barra de progreso
            val pasos = 5
            for (i in 1..pasos) {
                delay(400) // Pausa de medio segundo por paso
                _progreso.value = i.toFloat() / pasos.toFloat()
            }

            // Leemos el DataStore en segundo plano por si lo necesitamos guardar
            val guardado = estadoDataStore.obtenerEstado().first() ?: false

            // Indicamos que la carga terminó
            _cargaFinalizada.value = true
        }
    }
}