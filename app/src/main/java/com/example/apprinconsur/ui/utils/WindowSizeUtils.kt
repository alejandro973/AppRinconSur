package com.example.apprinconsur.ui.utils

import androidx.activity.compose.LocalActivity
// Permite acceder a la actividad actual (MainActivity) donde se está mostrando la interfaz de usuario en este preciso momento.
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
// Importa las herramientas oficiales de Material 3 diseñadas específicamente para medir el tamaño de las pantallas de los dispositivos.
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

// Esta anotación le avisa a Kotlin que estamos utilizando una característica experimental
// Android exige colocar esta etiqueta para confirmar que sabemos que podría tener cambios futuros, evitando que el compilador arroje advertencias.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as android.app.Activity)
}

// LocalActivity.current rescata la pantalla o actividad que el usuario está viendo en este instante.
// as android.app.Activity asegura que el formato sea compatible con el sistema operativo.
// calculateWindowSizeClass(...) toma esa actividad, mide de inmediato los píxeles reales de ancho y alto
// del dispositivo físico (o del emulador), y los clasifica automáticamente en una de las tres categorías estándar de Material 3: Compact (celulares en vertical), Medium (tablets o plegables) o Expanded (pantallas grandes o tablets horizontales).