package com.example.ministore.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ministore.presentation.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavigationItem(
            title = "Dashboard",
            icon = Icons.Default.Dashboard,
            route = Screen.Dashboard.route
        ),
        NavigationItem(
            title = "Inventory",
            icon = Icons.Default.Inventory,
            route = Screen.Inventory.route
        ),
        NavigationItem(
            title = "Sales",
            icon = Icons.Default.ShoppingCart,
            route = Screen.Sales.route
        ),
        NavigationItem(
            title = "Purchases",
            icon = Icons.Default.LocalShipping,
            route = Screen.Purchases.route
        ),
        NavigationItem(
            title = "Orders",
            icon = Icons.Default.ListAlt,
            route = Screen.Orders.route
        ),
        NavigationItem(
            title = "Clients",
            icon = Icons.Default.People,
            route = Screen.Clients.route
        ),
        NavigationItem(
            title = "Providers",
            icon = Icons.Default.Business,
            route = Screen.Providers.route
        ),
        NavigationItem(
            title = "Reports",
            icon = Icons.Default.Assessment,
            route = Screen.Reports.route
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            route = Screen.Settings.route
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Dashboard.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = content
    )
}

private data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) 