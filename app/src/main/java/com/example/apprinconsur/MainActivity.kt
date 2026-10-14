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
import com.example.apprinconsur.ui.screens.HomeScreen
import com.example.apprinconsur.ui.screens.ProfileScreen
import com.example.apprinconsur.ui.screens.SettingsScreen
import com.example.apprinconsur.ui.theme.AppRinconSurTheme
import com.example.apprinconsur.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    // Inicializamos nuestro ViewModel centralizado
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRinconSurTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Escuchamos los eventos de navegación que emite el ViewModel
                    LaunchedEffect(key1 = true) {
                        mainViewModel.navigationEvents.collectLatest { event ->
                            when (event) {
                                is NavigationEvent.NavigateTo -> {
                                    navController.navigate(event.route.route) {
                                        // Configuraciones opcionales del evento
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

                    // Definimos el mapa de pantallas (NavHost) usando los nombres correctos de Screen
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        // Ruta de Inicio
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                onNavigateToProfile = {
                                    mainViewModel.navigateTo(Screen.ProfileScreen)
                                }
                            )
                        }

                        // Ruta de Perfil
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

                        // Ruta de Configuración
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