package com.example.apprinconsur.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apprinconsur.data.AppDatabase
import com.example.apprinconsur.data.ProfileRepository
import com.example.apprinconsur.model.ProfileErrores
import com.example.apprinconsur.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.get(application)
    private val repository = ProfileRepository(database.profileDao())

    // Estado centralizado de la pantalla de perfil
    private val _estado = MutableStateFlow(ProfileUiState())
    val estado: StateFlow<ProfileUiState> = _estado.asStateFlow()

    init {
        // Carga automática si ya existe un perfil guardado en la base de datos
        viewModelScope.launch {
            repository.profileFlow.collect { profile ->
                if (profile != null) {
                    _estado.update {
                        it.copy(
                            nombre = profile.nombre,
                            correo = profile.correo,
                            rutaImagen = profile.rutaImagen
                        )
                    }
                }
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        _estado.update {
            it.copy(
                nombre = nuevoNombre,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.update {
            it.copy(
                correo = nuevoCorreo,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onRutaImagenChange(nuevaRuta: String) {
        _estado.update { it.copy(rutaImagen = nuevaRuta) }
    }

    // 📸 Función para procesar y registrar la foto capturada por la cámara
    fun onFotoCapturada(bitmap: Bitmap) {
        _estado.update {
            it.copy(rutaImagen = "Foto capturada exitosamente")
        }
    }

    // Validación de campos obligatorios
    fun validarFormulario(): Boolean {
        val current = _estado.value
        val errorNombre = if (current.nombre.isBlank()) "El nombre no puede estar vacío" else null
        val errorCorreo = if (current.correo.isBlank()) "El correo no puede estar vacío" else null

        val hayErrores = errorNombre != null || errorCorreo != null

        _estado.update {
            it.copy(errores = ProfileErrores(nombre = errorNombre, correo = errorCorreo))
        }

        return !hayErrores
    }

    // Guardar en la base de datos Room
    fun guardarPerfil(onSuccess: () -> Unit = {}) {
        if (validarFormulario()) {
            viewModelScope.launch {
                try {
                    val current = _estado.value
                    repository.guardarPerfil(
                        nombre = current.nombre,
                        correo = current.correo,
                        rutaImagen = current.rutaImagen
                    )
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}