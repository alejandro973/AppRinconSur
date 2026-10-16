package com.example.apprinconsur.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apprinconsur.model.RegistroErrores
import com.example.apprinconsur.model.RegistroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RegistroViewModel : ViewModel() {
    // Estado interno mutable del formulario
    private val _estado = MutableStateFlow(RegistroUiState())

    // Estado expuesto de solo lectura para la interfaz
    val estado: StateFlow<RegistroUiState> = _estado

    // Actualiza el tipo de flujo (Tránsito / Compra) y limpia su error
    fun onTipoFlujoChange(valor: String) {
        _estado.update {
            it.copy(tipoFlujo = valor, errores = it.errores.copy(tipoFlujo = null))
        }
    }

    // Actualiza el rango etario y limpia su error
    fun onRangoEtarioChange(valor: String) {
        _estado.update {
            it.copy(rangoEtario = valor, errores = it.errores.copy(rangoEtario = null))
        }
    }

    // Actualiza la cantidad de personas y limpia su error
    fun onCantidadPersonasChange(valor: String) {
        _estado.update {
            it.copy(cantidadPersonas = valor, errores = it.errores.copy(cantidadPersonas = null))
        }
    }

    // Actualiza las observaciones opcionales
    fun onObservacionesChange(valor: String) {
        _estado.update {
            it.copy(observaciones = valor)
        }
    }

    // Validación del formulario de conteo
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = RegistroErrores(
            tipoFlujo = if (estadoActual.tipoFlujo.isBlank()) "Debe seleccionar si es Tránsito o Compra" else null,
            rangoEtario = if (estadoActual.rangoEtario.isBlank()) "Debe seleccionar un rango etario" else null,
            cantidadPersonas = if (estadoActual.cantidadPersonas.toIntOrNull() == null || estadoActual.cantidadPersonas.toInt() <= 0) {
                "Ingrese una cantidad válida mayor a 0"
            } else null
        )

        val hayErrores = listOfNotNull(
            errores.tipoFlujo,
            errores.rangoEtario,
            errores.cantidadPersonas
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}