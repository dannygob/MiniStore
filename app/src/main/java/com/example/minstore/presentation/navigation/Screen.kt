package com.example.minstore.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Inventory : Screen("inventory")
    object Sales : Screen("sales")
    object Purchases : Screen("purchases")
    object Orders : Screen("orders")
    object Reports : Screen("reports")
    object Clients : Screen("clients")
    object Providers : Screen("providers")
    object Settings : Screen("settings")

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Dashboard.route -> Dashboard
                Inventory.route -> Inventory
                Sales.route -> Sales
                Purchases.route -> Purchases
                Orders.route -> Orders
                Reports.route -> Reports
                Clients.route -> Clients
                Providers.route -> Providers
                Settings.route -> Settings
                else -> Dashboard
            }
        }
    }
} 