package com.example.apprinconsur.navigation

// Representa los distintos tipos de eventos de navegación
sealed class NavigationEvent {
    /**
     * Evento para navegar a un destino específico.
     * @param route La ruta (objeto Screen) a la que navegar.
    // @param popupToRoute Ruta límite en el historial; elimina de la pila todas las pantallas intermedias hasta llegar a ella.
     * @param inclusive Si es 'true', la ruta especificada en [popupToRoute] también se elimina de la pila.
     * @param singleTop Si es 'true', evita múltiples copias del mismo destino en la parte superior de la pila
     */
    data class NavigateTo(
        val route: Screen,
        val popupToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para volver a la pantalla anterior en la pila de navegación
     */
    object PopBackStack : NavigationEvent()

    /**
     * Evento para navegar "hacia arriba" en la jerarquía de la aplicación
     */
    object NavigateUp : NavigationEvent()
}