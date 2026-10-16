package com.example.apprinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
    val estado by viewModel.estado.collectAsState()
    val scrollState = rememberScrollState()

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

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Tipo de Flujo (Tránsito vs Compra)
            Text(
                text = "Tipo de Flujo de Cliente *",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.onTipoFlujoChange("Tránsito") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estado.tipoFlujo == "Tránsito") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Tránsito", color = if (estado.tipoFlujo == "Tránsito") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = { viewModel.onTipoFlujoChange("Compra") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estado.tipoFlujo == "Compra") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Compra", color = if (estado.tipoFlujo == "Compra") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (estado.errores.tipoFlujo != null) {
                Text(
                    text = estado.errores.tipoFlujo!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
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
                            selected = estado.rangoEtario == rango,
                            onClick = { viewModel.onRangoEtarioChange(rango) }
                        )
                        Text(text = rango, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (estado.errores.rangoEtario != null) {
                Text(
                    text = estado.errores.rangoEtario!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Cantidad de personas
            OutlinedTextField(
                value = estado.cantidadPersonas,
                onValueChange = { viewModel.onCantidadPersonasChange(it) },
                label = { Text(text = "Cantidad de personas *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = estado.errores.cantidadPersonas != null,
                supportingText = {
                    estado.errores.cantidadPersonas?.let { errorMsg ->
                        Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Observaciones opcionales
            OutlinedTextField(
                value = estado.observaciones,
                onValueChange = { viewModel.onObservacionesChange(it) },
                label = { Text(text = "Observaciones (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar Registro
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        onRegistroExitoso()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar y Registrar")
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