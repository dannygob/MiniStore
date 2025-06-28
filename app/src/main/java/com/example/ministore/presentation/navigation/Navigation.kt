package com.example.ministore.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ministore.presentation.screen.clients.ClientsScreen
import com.example.ministore.presentation.screen.dashboard.DashboardScreen
import com.example.ministore.presentation.screen.orders.OrdersScreen
import com.example.ministore.presentation.screen.providers.ProvidersScreen
import com.example.ministore.presentation.screen.purchases.PurchasesScreen
import com.example.ministore.presentation.screen.settings.SettingsScreen
import com.example.ministore.presentation.screens.inventory.InventoryScreen
import com.example.ministore.presentation.screens.sales.SalesScreen
import com.ministore.presentation.reports.ReportsScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(Screen.Inventory.route) {
            InventoryScreen()
        }

        composable(Screen.Sales.route) {
            SalesScreen(navController)
        }

        composable(Screen.Purchases.route) {
            PurchasesScreen(navController)
        }

        composable(Screen.Orders.route) {
            OrdersScreen(navController)
        }

        composable(Screen.Clients.route) {
            ClientsScreen(navController)
        }

        composable(Screen.Providers.route) {
            ProvidersScreen(navController)
        }

        composable(Screen.Reports.route) {
            ReportsScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        // Detail screens
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            ProductDetailScreen(navController, productId)
        }

        composable(
            route = Screen.SaleDetail.route,
            arguments = listOf(navArgument("saleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getString("saleId") ?: return@composable
            SaleDetailScreen(navController, saleId)
        }

        composable(
            route = Screen.PurchaseDetail.route,
            arguments = listOf(navArgument("purchaseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val purchaseId = backStackEntry.arguments?.getString("purchaseId") ?: return@composable
            PurchaseDetailScreen(navController, purchaseId)
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderDetailScreen(navController, orderId)
        }

        composable(
            route = Screen.ClientDetail.route,
            arguments = listOf(navArgument("clientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            ClientDetailScreen(navController, clientId)
        }

        composable(
            route = Screen.ProviderDetail.route,
            arguments = listOf(navArgument("providerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: return@composable
            ProviderDetailScreen(navController, providerId)
        }
    }
} 