package com.example.apprinconsur.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.apprinconsur.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()
    val scrollState = rememberScrollState()

    // 📸 Lanzador de la cámara para capturar una vista previa de la foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            viewModel.onFotoCapturada(bitmap)
        }
    }

    // 🛡️ Lanzador para solicitar el permiso de la cámara en tiempo de ejecución
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el usuario acepta, lanzamos la cámara inmediatamente
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Se requiere el permiso de la cámara para tomar la foto.",
                Toast.LENGTH_SHORT
            ).show()
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
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para activar la cámara del celular con validación de permisos
            Button(
                onClick = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // Ya tenemos el permiso concedido, abrimos la cámara directamente
                            cameraLauncher.launch(null)
                        }
                        else -> {
                            // No tenemos el permiso, lo solicitamos en pantalla
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tomar Foto con Cámara")
            }

            // Muestra un texto indicando si ya se capturó la foto
            if (estado.rutaImagen.isNotEmpty()) {
                Text(
                    text = "Estado de foto: ${estado.rutaImagen}",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre *") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.nombre != null
            )
            if (estado.errores.nombre != null) {
                Text(
                    text = estado.errores.nombre!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = { Text("Correo *") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.correo != null
            )
            if (estado.errores.correo != null) {
                Text(
                    text = estado.errores.correo!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar
            Button(
                onClick = {
                    viewModel.guardarPerfil(
                        onSuccess = {
                            onNavigateBack()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Guardar Perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancelar / Volver")
            }
        }
    }
}