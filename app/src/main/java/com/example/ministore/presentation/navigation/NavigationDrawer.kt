package com.example.ministore.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navController: NavController,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MiniStore",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Screen.drawerScreens.forEach { screen ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Dashboard -> Icons.Default.Dashboard
                                    Screen.Inventory -> Icons.Default.Inventory
                                    Screen.Sales -> Icons.Default.ShoppingCart
                                    Screen.Purchases -> Icons.Default.Receipt
                                    Screen.Orders -> Icons.Default.ListAlt
                                    Screen.Clients -> Icons.Default.People
                                    Screen.Providers -> Icons.Default.Business
                                    Screen.Reports -> Icons.Default.BarChart
                                    Screen.Settings -> Icons.Default.Settings
                                },
                                contentDescription = screen.route
                            )
                        },
                        label = { Text(text = screen.route.capitalize()) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
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