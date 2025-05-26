package com.ministore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ministore.presentation.navigation.Navigation
import com.ministore.presentation.navigation.Screen
import com.ministore.presentation.theme.MiniStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniStoreTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = remember(currentDestination) {
        currentDestination?.hierarchy?.any { destination ->
            listOf(
                Screen.Dashboard.route,
                Screen.Products.route,
                Screen.SalesHistory.route
            ).contains(destination.route)
        } ?: false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.route == Screen.Dashboard.route,
                        onClick = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Dashboard, contentDescription = "Dashboard")
                        },
                        label = { Text("Dashboard") }
                    )
                    NavigationBarItem(
                        selected = currentDestination?.route == Screen.Products.route,
                        onClick = {
                            navController.navigate(Screen.Products.route) {
                                popUpTo(Screen.Products.route) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Inventory, contentDescription = "Products")
                        },
                        label = { Text("Products") }
                    )
                    NavigationBarItem(
                        selected = currentDestination?.route == Screen.SalesHistory.route,
                        onClick = {
                            navController.navigate(Screen.SalesHistory.route) {
                                popUpTo(Screen.SalesHistory.route) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Receipt, contentDescription = "Sales")
                        },
                        label = { Text("Sales") }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Navigation(navController = navController)
        }
    }
} 