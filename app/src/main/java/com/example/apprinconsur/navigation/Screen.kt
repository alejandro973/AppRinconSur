package com.example.apprinconsur.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen(route = "home_page")
    data object ProfileScreen : Screen(route = "profile_page")
    data object SettingsScreen : Screen(route = "settings_page")
}