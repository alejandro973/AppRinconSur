package com.example.apprinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.apprinconsur.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit, // Función lambda para navegar cuando el login sea exitoso
    onNavigateToProfile: () -> Unit // 👈 Nueva función lambda para ir a la creación de perfil
) {
    // Observamos el estado reactivo del ViewModel
    val estado by viewModel.estado.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de bienvenida a la app
            Text(
                text = "Rincón Sur - Acceso Personal",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Correo Electrónico Corporativo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = { Text(text = "Correo electrónico") },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { errorMsg ->
                        Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = estado.clave,
                onValueChange = { viewModel.onClaveChange(it) },
                label = { Text(text = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let { errorMsg ->
                        Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    // Si la validación global es exitosa, ejecutamos la acción de éxito (navegación)
                    if (viewModel.validarFormulario()) {
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 👤 Botón secundario para registrarse / crear usuario por primera vez
            OutlinedButton(
                onClick = {
                    onNavigateToProfile()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}