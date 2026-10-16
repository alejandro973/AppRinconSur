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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apprinconsur.navigation.NavigationEvent
import com.example.apprinconsur.navigation.Screen
import com.example.apprinconsur.ui.screens.*
import com.example.apprinconsur.ui.theme.AppRinconSurTheme
import com.example.apprinconsur.viewmodel.LoginViewModel
import com.example.apprinconsur.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

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
                                    // Al hacer login exitoso, vamos directo a la selección de accesos de la tienda
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
                                    // Aquí luego conectaremos con el formulario de la Guía 11
                                    // Por ahora, podemos navegar al Home o preparar la ruta de registro
                                    mainViewModel.navigateTo(Screen.HomeScreen)
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. Pantalla de Inicio / Dashboard (Opcional en el flujo)
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                onNavigateToProfile = {
                                    mainViewModel.navigateTo(Screen.ProfileScreen)
                                }
                            )
                        }

                        // 4. Pantalla de Perfil
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

                        // 5. Pantalla de Configuración
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