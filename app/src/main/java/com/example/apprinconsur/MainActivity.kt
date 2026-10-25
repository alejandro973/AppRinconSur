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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apprinconsur.navigation.NavigationEvent
import com.example.apprinconsur.navigation.Screen
import com.example.apprinconsur.ui.screens.*
import com.example.apprinconsur.ui.theme.AppRinconSurTheme
import com.example.apprinconsur.viewmodel.EstadoViewModel
import com.example.apprinconsur.viewmodel.LoginViewModel
import com.example.apprinconsur.viewmodel.MainViewModel
import com.example.apprinconsur.viewmodel.ProfileViewModel
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
                        startDestination = "estado_screen" // 👈 La app arranca con la barra de carga animada
                    ) {
                        // 0. Pantalla de Carga Inicial (Splash con barra de progreso)
                        composable(route = "estado_screen") {
                            val estadoViewModel: EstadoViewModel = viewModel()
                            EstadoScreen(
                                viewModel = estadoViewModel,
                                onNavigateToLogin = {
                                    navController.navigate(Screen.LoginScreen.route) {
                                        // Limpiamos del historial para que al presionar "atrás" no regrese a la carga
                                        popUpTo("estado_screen") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 1. Pantalla de Inicio de Sesión (Con acceso al registro de perfil)
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.AccessSelectionScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                    }
                                },
                                onNavigateToProfile = {
                                    navController.navigate(Screen.ProfileScreen.route)
                                }
                            )
                        }

                        // 2. Pantalla de Selección de Accesos
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
                                    navController.navigate(Screen.ResumenScreen.route) {
                                        popUpTo("registro_screen/{nombreAcceso}") { inclusive = false }
                                    }
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 4. Pantalla de Resumen
                        composable(route = Screen.ResumenScreen.route) {
                            ResumenScreen(
                                mainViewModel = mainViewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 5. Pantalla de Perfil (Con inyección de su ProfileViewModel y cámara)
                        composable(route = Screen.ProfileScreen.route) {
                            val profileViewModel: ProfileViewModel = viewModel()
                            ProfileScreen(
                                viewModel = profileViewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 6. Pantalla de Configuración
                        composable(route = Screen.SettingsScreen.route) {
                            SettingsScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}