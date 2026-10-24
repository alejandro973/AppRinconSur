package com.example.apprinconsur.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apprinconsur.viewmodel.EstadoViewModel

@Composable
fun EstadoScreen(
    modifier: Modifier = Modifier,
    viewModel: EstadoViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {
    val progreso by viewModel.progreso.collectAsState()
    val cargaFinalizada by viewModel.cargaFinalizada.collectAsState()

    // Animación suave para la barra de progreso
    val progresoAnimado by animateFloatAsState(
        targetValue = progreso,
        label = "ProgresoCarga"
    )

    // Cuando la carga finaliza, navegamos automáticamente al Login
    LaunchedEffect(cargaFinalizada) {
        if (cargaFinalizada) {
            onNavigateToLogin()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título o Logo de presentación
            Text(
                text = "Rincón Sur",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Cargando sistema...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Barra de progreso lineal animada
            LinearProgressIndicator(
                progress = { progresoAnimado },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Porcentaje de carga visual
            Text(
                text = "${(progresoAnimado * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}