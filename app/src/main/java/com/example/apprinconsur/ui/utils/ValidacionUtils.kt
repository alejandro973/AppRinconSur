package com.example.apprinconsur.ui.utils

fun correoValido(correo: String): Boolean {
    val limpio = correo.trim()
    val posicion = limpio.indexOf('@')
    return posicion > 0 &&
            posicion < limpio.length - 1 &&
            limpio.count { it == '@' } == 1 &&
            limpio.none { it.isWhitespace() }
}