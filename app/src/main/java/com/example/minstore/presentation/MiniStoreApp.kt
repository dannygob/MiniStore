package com.example.minstore.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minstore.presentation.navigation.Screen

@Composable
fun MiniStoreApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            // DashboardScreen(navController)
        }
        composable(Screen.Inventory.route) {
            // InventoryScreen(navController)
        }
        composable(Screen.Sales.route) {
            // SalesScreen(navController)
        }
        composable(Screen.Purchases.route) {
            // PurchasesScreen(navController)
        }
        composable(Screen.Orders.route) {
            // OrdersScreen(navController)
        }
        composable(Screen.Reports.route) {
            // ReportsScreen(navController)
        }
        composable(Screen.Clients.route) {
            // ClientsScreen(navController)
        }
        composable(Screen.Providers.route) {
            // ProvidersScreen(navController)
        }
        composable(Screen.Settings.route) {
            // SettingsScreen(navController)
        }
    }
} 