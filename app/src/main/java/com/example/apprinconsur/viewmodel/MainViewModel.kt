package com.example.apprinconsur.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.apprinconsur.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.apprinconsur.navigation.NavigationEvent

/** Estructura de datos individual para cada registro mostrado en el resumen */
data class RegistroItem(
    val nombreAcceso: String,
    val tipoFlujo: String,
    val movimiento: String,
    val rangoEtario: String,
    val cantidadPersonas: Int,
    val observaciones: String
)

/** Si las pantallas son las que piden viajar y los eventos son las órdenes de viaje,
- el MainViewModel es el cerebro o director de orquesta que coordina todo esto de forma limpia y segura.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // --- GESTIÓN DE REGISTROS (Para ResumenScreen) ---
    private val _registrosList = MutableStateFlow<List<RegistroItem>>(emptyList())
    val registrosList: StateFlow<List<RegistroItem>> = _registrosList.asStateFlow()

    fun agregarRegistro(
        nombreAcceso: String,
        tipoFlujo: String,
        movimiento: String,
        rangoEtario: String,
        cantidadPersonas: Int,
        observaciones: String
    ) {
        val nuevoRegistro = RegistroItem(
            nombreAcceso = nombreAcceso,
            tipoFlujo = tipoFlujo,
            movimiento = movimiento,
            rangoEtario = rangoEtario,
            cantidadPersonas = cantidadPersonas,
            observaciones = observaciones
        )
        // Añadimos el nuevo elemento a la lista reactiva
        _registrosList.value = _registrosList.value + nuevoRegistro
    }

    // --- GESTIÓN DE NAVEGACIÓN ---
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun navigateTo(route: Screen, singleTop: Boolean = false) {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = route, singleTop = singleTop))
        }
    }

    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}