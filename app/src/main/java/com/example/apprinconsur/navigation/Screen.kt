package com.example.apprinconsur.navigation

sealed class Screen(val route: String) {
    // Rutas simples o estáticas (sin argumentos)
    data object HomeScreen : Screen(route = "home_page")
    data object ProfileScreen : Screen(route = "profile_page")
    data object SettingsScreen : Screen(route = "settings_page")

    // Ejemplo de Ruta con Argumentos (para cuando necesites pasar un ID, por ejemplo, de un producto)
    data class Detail(val itemId: String) : Screen(route = "detail_page/{itemId}") {
        // Función para construir la ruta final reemplazando el parámetro
        fun buildRoute(): String {
            return route.replace(oldValue = "{itemId}", newValue = itemId)
        }
    }
}

//La clase hereda la ruta base detail_page/{itemId} desde Screen y, justo gracias a la función buildRoute(),
// toma ese valor dinámico que recibe por parámetro (itemId) para reemplazar el comodín {itemId} y armar la ruta final lista para que
// el sistema de navegación la ejecute sin errores.