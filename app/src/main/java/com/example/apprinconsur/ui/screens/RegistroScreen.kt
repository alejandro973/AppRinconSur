package com.example.apprinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.apprinconsur.ui.utils.formatearHora
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.apprinconsur.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    nombreAcceso: String, // Recibe el acceso seleccionado (ej: "Acceso 1")
    viewModel: RegistroViewModel,
    onRegistroExitoso: () -> Unit, // Acción al guardar con éxito
    onNavigateBack: () -> Unit      // Para volver atrás
) {
    // Estados locales para los campos del formulario conectados a la base de datos
    var movimiento by remember { mutableStateOf("Ingreso") }
    var tipoFlujo by remember { mutableStateOf("Tránsito") }
    var rangoEtario by remember { mutableStateOf("Adulto") }
    var cantidadPersonas by remember { mutableStateOf(1) }
    var observaciones by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Reloj en tiempo real para la interfaz
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis()
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la pantalla
            Text(
                text = "Registro de Flujo",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtítulo indicando en qué acceso se está operando
            Text(
                text = "Ubicación actual: $nombreAcceso",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Hora del dispositivo: ${formatearHora(ahora)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Movimiento *",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Ingreso", "Salida").forEach { opcion ->
                    Button(
                        onClick = { movimiento = opcion },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (movimiento == opcion) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = opcion,
                            color = if (movimiento == opcion) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Sección: resultado de la visita (solo al registrar una Salida)
            if (movimiento == "Salida") {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "¿La persona compró? *",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Compra" to "Con compra", "Tránsito" to "Sin compra (solo paso)").forEach { (valor, etiqueta) ->
                        Button(
                            onClick = { tipoFlujo = valor },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tipoFlujo == valor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = etiqueta,
                                color = if (tipoFlujo == valor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección: Rango Etario
            Text(
                text = "Rango Etario *",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            val rangos = listOf("Joven", "Adulto", "Adulto Mayor")
            Column(modifier = Modifier.fillMaxWidth()) {
                rangos.forEach { rango ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = rangoEtario == rango,
                            onClick = { rangoEtario = rango }
                        )
                        Text(text = rango, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cantidad de personas *",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (cantidadPersonas > 1) cantidadPersonas-- },
                    enabled = cantidadPersonas > 1,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text(text = "-", style = MaterialTheme.typography.headlineMedium)
                }
                Text(
                    text = cantidadPersonas.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(96.dp)
                )
                Button(
                    onClick = { cantidadPersonas++ },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text(text = "+", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Observaciones opcionales
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text(text = "Observaciones (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar Registro (Inserta directamente en la BD SQLite con el ViewModel)
            Button(
                onClick = {
                    viewModel.guardarRegistro(
                        nombreAcceso = nombreAcceso,
                        tipoFlujo = tipoFlujo,
                        movimiento = movimiento,
                        rangoEtario = rangoEtario,
                        cantidadPersonas = cantidadPersonas,
                        observaciones = observaciones,
                        onSuccess = {
                            onRegistroExitoso()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar y Registrar en SQLite")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Volver
            OutlinedButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancelar / Volver")
            }
        }
    }
}