package com.example.apprinconsur.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apprinconsur.navigation.NavigationEvent
import com.example.apprinconsur.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**Si las pantallas son las que piden viajar y los eventos son las órdenes de viaje,
 *  el MainViewModel es el cerebro o director de orquesta que coordina todo esto de forma limpia y segura.
 */

class MainViewModel : ViewModel() {

    // Canal privado mutable para emitir los eventos de navegación
    /*
    Es una tubería interna y privada donde el ViewModel anota las órdenes de viaje.
     Al ser privada, ninguna pantalla puede corromperla o enviar órdenes a la fuerza por su cuenta.
     */
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    // Canal público de solo lectura para que la UI pueda observarlo de manera segura
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // Función que emite el evento para navegar hacia una pantalla específica
    fun navigateTo(screen: Screen) {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    // Función para emitir el evento de volver atrás
    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    // Función para emitir el evento de navegar hacia arriba (jerarquía padre)
    fun navigateUp() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}