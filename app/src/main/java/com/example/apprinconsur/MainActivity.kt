package com.example.apprinconsur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apprinconsur.navigation.NavigationEvent
import com.example.apprinconsur.navigation.Screen
import com.example.apprinconsur.ui.screens.*
import com.example.apprinconsur.ui.theme.AppRinconSurTheme
import com.example.apprinconsur.viewmodel.LoginViewModel
import com.example.apprinconsur.viewmodel.MainViewModel
import com.example.apprinconsur.viewmodel.RegistroViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRinconSurTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(key1 = true) {
                        mainViewModel.navigationEvents.collectLatest { event ->
                            when (event) {
                                is NavigationEvent.NavigateTo -> {
                                    navController.navigate(event.route.route) {
                                        if (event.singleTop) launchSingleTop = true
                                    }
                                }
                                is NavigationEvent.PopBackStack -> {
                                    navController.popBackStack()
                                }
                                is NavigationEvent.NavigateUp -> {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen.route
                    ) {
                        // 1. Pantalla de Inicio de Sesión
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.AccessSelectionScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Pantalla de Selección de Accesos (1 al 4)
                        composable(route = Screen.AccessSelectionScreen.route) {
                            AccessSelectionScreen(
                                onNavigateToRegistro = { accesoSeleccionado ->
                                    navController.navigate("registro_screen/$accesoSeleccionado")
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. Pantalla de Registro de Flujo
                        composable(
                            route = "registro_screen/{nombreAcceso}",
                            arguments = listOf(navArgument("nombreAcceso") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val nombreAcceso = backStackEntry.arguments?.getString("nombreAcceso") ?: "Acceso General"
                            RegistroScreen(
                                nombreAcceso = nombreAcceso,
                                viewModel = registroViewModel,
                                onRegistroExitoso = {
                                    // Obtenemos el estado actual directo desde el registroViewModel
                                    val estadoActual = registroViewModel.estado.value

                                    // Guardamos el registro en el MainViewModel
                                    mainViewModel.agregarRegistro(
                                        nombreAcceso = nombreAcceso,
                                        tipoFlujo = estadoActual.tipoFlujo,
                                        movimiento = estadoActual.movimiento,
                                        rangoEtario = estadoActual.rangoEtario,
                                        cantidadPersonas = estadoActual.cantidadPersonas,
                                        observaciones = estadoActual.observaciones
                                    )
                                    registroViewModel.reiniciar()
                                    // Navegamos a la pantalla de resumen
                                    navController.navigate("resumen_screen")
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 4. Pantalla de Resumen de Registros acumulados
                        composable(route = "resumen_screen") {
                            ResumenScreen(
                                mainViewModel = mainViewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 5. Pantalla de Inicio / Dashboard (Opcional)
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                onNavigateToProfile = {
                                    mainViewModel.navigateTo(Screen.ProfileScreen)
                                }
                            )
                        }

                        // 6. Pantalla de Perfil
                        composable(route = Screen.ProfileScreen.route) {
                            ProfileScreen(
                                onNavigateBack = {
                                    mainViewModel.navigateBack()
                                },
                                onNavigateToSettings = {
                                    mainViewModel.navigateTo(Screen.SettingsScreen)
                                }
                            )
                        }

                        // 7. Pantalla de Configuración
                        composable(route = Screen.SettingsScreen.route) {
                            SettingsScreen(
                                onNavigateBack = {
                                    mainViewModel.navigateBack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}