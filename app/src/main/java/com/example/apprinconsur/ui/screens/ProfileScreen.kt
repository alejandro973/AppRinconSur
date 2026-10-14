package com.example.apprinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    // Función para manejar el evento de volver atrás
    onNavigateBack: () -> Unit,
    // Función para navegar a la configuración
    onNavigateToSettings: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Estás en la Pantalla de Perfil!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onNavigateToSettings) {
                Text(text = "Ir a Configuración")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = onNavigateBack) {
                Text(text = "Volver Atrás")
            }
        }
    }
}