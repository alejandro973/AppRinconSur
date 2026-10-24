package com.example.apprinconsur.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore con el nombre de preferencias de usuario
val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context) {

    companion object {
        private val ESTADO_ACTIVADO = booleanPreferencesKey(name = "modo_activado")
    }

    // Función para guardar el estado en DataStore
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    // Función para obtener el estado de forma reactiva (Flow)
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }
}