// Requisito: formato de la hora local del dispositivo (zona horaria y locale del sistema)
package com.example.apprinconsur.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatearFechaHora(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(millis))

fun formatearHora(millis: Long): String =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(millis))
