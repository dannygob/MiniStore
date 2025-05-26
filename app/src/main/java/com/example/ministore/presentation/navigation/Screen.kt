package com.example.ministore.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Inventory : Screen("inventory")
    object Sales : Screen("sales")
    object Purchases : Screen("purchases")
    object Orders : Screen("orders")
    object Clients : Screen("clients")
    object Providers : Screen("providers")
    object Reports : Screen("reports")
    object Settings : Screen("settings")

    // Detail screens
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }

    object SaleDetail : Screen("sale/{saleId}") {
        fun createRoute(saleId: String) = "sale/$saleId"
    }

    object PurchaseDetail : Screen("purchase/{purchaseId}") {
        fun createRoute(purchaseId: String) = "purchase/$purchaseId"
    }

    object OrderDetail : Screen("order/{orderId}") {
        fun createRoute(orderId: String) = "order/$orderId"
    }

    object ClientDetail : Screen("client/{clientId}") {
        fun createRoute(clientId: String) = "client/$clientId"
    }

    object ProviderDetail : Screen("provider/{providerId}") {
        fun createRoute(providerId: String) = "provider/$providerId"
    }

    companion object {
        val drawerScreens = listOf(
            Dashboard,
            Inventory,
            Sales,
            Purchases,
            Orders,
            Clients,
            Providers,
            Reports,
            Settings
        )
    }
} 