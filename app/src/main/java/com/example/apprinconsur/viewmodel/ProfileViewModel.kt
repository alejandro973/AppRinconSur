package com.example.apprinconsur.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.apprinconsur.data.AppDatabase
import com.example.apprinconsur.data.ProfileRepository
import com.example.apprinconsur.model.ProfileErrores
import com.example.apprinconsur.model.ProfileUiState
import com.example.apprinconsur.ui.utils.correoValido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ProfileViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val database = AppDatabase.get(application)
    private val repository = ProfileRepository(database.profileDao())

    private val _estado = MutableStateFlow(
        ProfileUiState(
            nombre = savedStateHandle[KEY_NOMBRE] ?: "",
            correo = savedStateHandle[KEY_CORREO] ?: "",
            rutaImagen = savedStateHandle[KEY_RUTA_IMAGEN] ?: ""
        )
    )
    val estado: StateFlow<ProfileUiState> = _estado.asStateFlow()

    init {
        if (!savedStateHandle.contains(KEY_BORRADOR)) {
            viewModelScope.launch {
                val profile = repository.profileFlow.first()
                if (profile != null && !savedStateHandle.contains(KEY_BORRADOR)) {
                    actualizar {
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

    private fun actualizar(transform: (ProfileUiState) -> ProfileUiState) {
        _estado.update(transform)
        val actual = _estado.value
        savedStateHandle[KEY_NOMBRE] = actual.nombre
        savedStateHandle[KEY_CORREO] = actual.correo
        savedStateHandle[KEY_RUTA_IMAGEN] = actual.rutaImagen
    }

    private fun marcarBorrador() {
        savedStateHandle[KEY_BORRADOR] = true
    }

    fun onNombreChange(nuevoNombre: String) {
        marcarBorrador()
        actualizar {
            it.copy(
                nombre = nuevoNombre,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        marcarBorrador()
        actualizar {
            it.copy(
                correo = nuevoCorreo,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onRutaImagenChange(nuevaRuta: String) {
        marcarBorrador()
        actualizar { it.copy(rutaImagen = nuevaRuta) }
    }

    fun onFotoCapturada(bitmap: Bitmap) {
        viewModelScope.launch {
            val ruta = withContext(Dispatchers.IO) {
                val directorio = directorioFotos().apply { mkdirs() }
                val archivo = File(directorio, "perfil_${System.currentTimeMillis()}.jpg")
                FileOutputStream(archivo).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
                archivo.absolutePath
            }
            marcarBorrador()
            actualizar { it.copy(rutaImagen = ruta) }
        }
    }

    private fun directorioFotos() = File(getApplication<Application>().filesDir, "fotos")

    private fun limpiarFotosAntiguas(rutaActual: String) {
        directorioFotos().listFiles()?.forEach { archivo ->
            if (archivo.absolutePath != rutaActual) archivo.delete()
        }
    }

    fun validarFormulario(): Boolean {
        val current = _estado.value
        val errorNombre = if (current.nombre.isBlank()) "El nombre no puede estar vacío" else null
        val errorCorreo = when {
            current.correo.isBlank() -> "El correo no puede estar vacío"
            !correoValido(current.correo) -> "Ingresa un correo válido que incluya @"
            else -> null
        }

        val hayErrores = errorNombre != null || errorCorreo != null

        _estado.update {
            it.copy(errores = ProfileErrores(nombre = errorNombre, correo = errorCorreo))
        }

        return !hayErrores
    }

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
                    withContext(Dispatchers.IO) { limpiarFotosAntiguas(current.rutaImagen) }
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private companion object {
        const val KEY_NOMBRE = "perfil_nombre"
        const val KEY_CORREO = "perfil_correo"
        const val KEY_RUTA_IMAGEN = "perfil_ruta_imagen"
        const val KEY_BORRADOR = "perfil_borrador"
    }
}