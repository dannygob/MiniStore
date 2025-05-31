package com.example.ministore.presentation

import android.app.Application
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.work.HiltWorkerFactory
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import com.example.ministore.presentation.navigation.Navigation
import com.example.ministore.presentation.navigation.NavigationDrawer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MiniStoreApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Composable
    fun MiniStoreAppView() {
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
}
