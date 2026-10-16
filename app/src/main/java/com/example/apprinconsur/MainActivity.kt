package com.example.apprinconsur

import com.example.apprinconsur.ui.theme.AppRinconSurTheme
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
import com.example.apprinconsur.viewmodel.LoginViewModel
import com.example.apprinconsur.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    // Inicializamos nuestros ViewModels
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

                    // Escuchamos los eventos de navegación globales
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

                    // Definimos el mapa de pantallas (NavHost)
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen.route // La app ahora inicia en el Login
                    ) {
                        // 1. Ruta de Inicio de Sesión (Login)
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    // Al validar con éxito, navegamos al Home principal y limpiamos el login del stack
                                    navController.navigate(Screen.HomeScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Ruta de Inicio (Dashboard de la app)
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                onNavigateToProfile = {
                                    mainViewModel.navigateTo(Screen.ProfileScreen)
                                }
                            )
                        }

                        // 3. Ruta de Perfil
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

                        // 4. Ruta de Configuración
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