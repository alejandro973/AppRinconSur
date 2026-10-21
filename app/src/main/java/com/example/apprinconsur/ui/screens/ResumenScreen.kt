package com.example.apprinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apprinconsur.model.RegistroItem
import com.example.apprinconsur.ui.utils.formatearFechaHora
import com.example.apprinconsur.viewmodel.MainViewModel

@Composable
fun ResumenScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit // Para volver al menú o pantallas anteriores
) {
    // Observamos la lista acumulada de registros desde el MainViewModel
    val registros by mainViewModel.registrosList.collectAsState()
    val totalIngresos = registros.filter { it.movimiento == "Ingreso" }.sumOf { it.cantidadPersonas }
    val totalSalidas = registros.filter { it.movimiento == "Salida" }.sumOf { it.cantidadPersonas }
    val salidasConCompra = registros.filter { it.movimiento == "Salida" && it.tipoFlujo == "Compra" }.sumOf { it.cantidadPersonas }
    val salidasSinCompra = registros.filter { it.movimiento == "Salida" && it.tipoFlujo == "Tránsito" }.sumOf { it.cantidadPersonas }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título de la pantalla
            Text(
                text = "Resumen de Registros",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Historial de flujo capturado en los accesos:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Ingresos: $totalIngresos", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Salidas: $totalSalidas", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Dentro: ${totalIngresos - totalSalidas}", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Con compra: $salidasConCompra", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Sin compra: $salidasSinCompra", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Si la lista está vacía, mostramos un mensaje informativo
            if (registros.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay registros guardados.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                // Lista de registros usando LazyColumn para mejor rendimiento
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(registros) { item ->
                        RegistroCard(item = item)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para volver atrás
            OutlinedButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver")
            }
        }
    }
}

// Tarjeta visual individual para cada registro en la lista
@Composable
fun RegistroCard(item: RegistroItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.nombreAcceso,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                // Indicador de tipo de flujo
                Badge {
                    Text(
                        text = when (item.tipoFlujo) {
                            "Compra" -> "${item.movimiento} · Con compra"
                            "Tránsito" -> "${item.movimiento} · Sin compra"
                            else -> item.movimiento
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rango etario: ${item.rangoEtario}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cantidad de personas: ${item.cantidadPersonas}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fecha y hora: ${formatearFechaHora(item.fechaHora)}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (item.observaciones.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Obs: ${item.observaciones}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}