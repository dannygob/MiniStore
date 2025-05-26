package com.ministore.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ministore.presentation.auth.LoginScreen
import com.ministore.presentation.auth.SignupScreen
import com.ministore.presentation.dashboard.DashboardScreen
import com.ministore.presentation.product.AddEditProductScreen
import com.ministore.presentation.product.ProductListScreen
import com.ministore.presentation.sale.SalesHistoryScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Products : Screen("products")
    object AddEditProduct : Screen("add_edit_product?productId={productId}") {
        fun createRoute(productId: String? = null) =
            "add_edit_product" + (productId?.let { "?productId=$it" } ?: "")
    }

    object SalesHistory : Screen("sales_history")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onSignupSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.Products.route)
                }
            )
        }

        composable(Screen.Products.route) {
            ProductListScreen(
                onAddProduct = {
                    navController.navigate(Screen.AddEditProduct.createRoute())
                },
                onEditProduct = { productId ->
                    navController.navigate(Screen.AddEditProduct.createRoute(productId))
                }
            )
        }

        composable(
            route = Screen.AddEditProduct.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            AddEditProductScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SalesHistory.route) {
            SalesHistoryScreen()
        }
    }
} 