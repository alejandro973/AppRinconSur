package com.example.apprinconsur.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apprinconsur.model.LoginErrores
import com.example.apprinconsur.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    // Estado interno mutable para controlar los datos y errores del formulario
    private val _estado = MutableStateFlow(LoginUiState())

    // Estado expuesto de manera segura y de solo lectura para la UI
    val estado: StateFlow<LoginUiState> = _estado

    // Actualiza el campo correo y limpia su error asociado
    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(correo = valor, errores = it.errores.copy(correo = null))
        }
    }

    // Actualiza el campo clave y limpia su error asociado
    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(clave = valor, errores = it.errores.copy(clave = null))
        }
    }

    // Validación global del formulario de inicio de sesión
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        // Definimos las reglas de validación para el trabajador de la tienda
        val errores = LoginErrores(
            correo = if (estadoActual.correo.isBlank()) "El correo corporativo es obligatorio" else null,
            clave = if (estadoActual.clave.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        )

        // Verificamos si existe algún error registrado
        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave
        ).isNotEmpty()

        // Actualizamos el estado con los errores detectados (si los hay)
        _estado.update { it.copy(errores = errores) }

        // Retorna true si el formulario es válido (no hay errores), false si falla
        return !hayErrores
    }
}