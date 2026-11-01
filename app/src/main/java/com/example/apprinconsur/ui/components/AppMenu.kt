package com.example.apprinconsur.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.apprinconsur.navigation.Screen
import kotlinx.coroutines.launch

private const val RUTA_REGISTRO = "registro_screen/{nombreAcceso}"

private val RUTAS_CON_MENU = setOf(
    Screen.AccessSelectionScreen.route,
    RUTA_REGISTRO,
    Screen.ResumenScreen.route,
    Screen.SettingsScreen.route
)

private data class DestinoMenu(val etiqueta: String, val ruta: String)

private val DESTINOS = listOf(
    DestinoMenu("Accesos", Screen.AccessSelectionScreen.route),
    DestinoMenu("Resumen de registros", Screen.ResumenScreen.route),
    DestinoMenu("Perfil", Screen.ProfileScreen.route),
    DestinoMenu("Configuración", Screen.SettingsScreen.route)
)

@Composable
private fun IconoHamburguesa() {
    val color = LocalContentColor.current
    Canvas(modifier = Modifier.size(24.dp)) {
        val grosor = 2.dp.toPx()
        val margen = 3.dp.toPx()
        listOf(0.28f, 0.5f, 0.72f).forEach { fraccion ->
            val y = size.height * fraccion
            drawLine(
                color = color,
                start = Offset(margen, y),
                end = Offset(size.width - margen, y),
                strokeWidth = grosor,
                cap = StrokeCap.Round
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMenu(
    navController: NavHostController,
    onCerrarSesion: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val mostrarMenu = rutaActual != null && rutaActual in RUTAS_CON_MENU

    val titulo = when (rutaActual) {
        Screen.AccessSelectionScreen.route -> "Accesos"
        RUTA_REGISTRO -> "Registro de flujo"
        Screen.ResumenScreen.route -> "Resumen"
        Screen.SettingsScreen.route -> "Configuración"
        else -> "Rincón Sur"
    }

    fun irA(ruta: String) {
        scope.launch { drawerState.close() }
        navController.navigate(ruta) {
            launchSingleTop = true
            if (ruta == Screen.AccessSelectionScreen.route) {
                popUpTo(ruta) { inclusive = false }
            }
        }
    }

    fun cerrarSesion() {
        scope.launch { drawerState.close() }
        onCerrarSesion()
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = mostrarMenu,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Rincón Sur",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                DESTINOS.forEach { destino ->
                    NavigationDrawerItem(
                        label = { Text(destino.etiqueta) },
                        selected = rutaActual == destino.ruta,
                        onClick = { irA(destino.ruta) },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", color = MaterialTheme.colorScheme.error) },
                    selected = false,
                    onClick = { cerrarSesion() },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (mostrarMenu) {
                    TopAppBar(
                        title = { Text(titulo) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                IconoHamburguesa()
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}