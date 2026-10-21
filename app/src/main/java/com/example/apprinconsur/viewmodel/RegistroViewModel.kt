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

    // Al elegir Ingreso se descarta el tipo de flujo: solo aplica a las salidas
    fun onMovimientoChange(valor: String) {
        _estado.update {
            it.copy(
                movimiento = valor,
                tipoFlujo = if (valor == "Salida") it.tipoFlujo else "",
                errores = it.errores.copy(movimiento = null, tipoFlujo = null)
            )
        }
    }

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

    fun onCantidadMas() {
        _estado.update { it.copy(cantidadPersonas = it.cantidadPersonas + 1) }
    }

    fun onCantidadMenos() {
        _estado.update {
            if (it.cantidadPersonas > 1) it.copy(cantidadPersonas = it.cantidadPersonas - 1) else it
        }
    }

    fun reiniciar() {
        _estado.value = RegistroUiState()
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
            movimiento = if (estadoActual.movimiento.isBlank()) "Debe seleccionar Ingreso o Salida" else null,
            tipoFlujo = if (estadoActual.movimiento == "Salida" && estadoActual.tipoFlujo.isBlank()) "Debe indicar si la salida fue con compra o sin compra" else null,
            rangoEtario = if (estadoActual.rangoEtario.isBlank()) "Debe seleccionar un rango etario" else null
        )

        val hayErrores = listOfNotNull(
            errores.movimiento,
            errores.tipoFlujo,
            errores.rangoEtario
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
