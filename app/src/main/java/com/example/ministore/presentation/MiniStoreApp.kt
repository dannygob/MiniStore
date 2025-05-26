package com.example.ministore.presentation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.ministore.presentation.navigation.Navigation
import com.example.ministore.presentation.navigation.NavigationDrawer

@Composable
fun MiniStoreApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        drawerState = drawerState,
        navController = navController,
        scope = scope
    ) {
        Navigation(navController = navController)
    }
} 