package com.ministore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ministore.presentation.auth.LoginScreen
import com.ministore.presentation.product.AddProductScreen
import com.ministore.presentation.product.EditProductScreen
import com.ministore.presentation.product.ProductListScreen
import com.ministore.presentation.theme.MiniStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.ProductList) }

                    if (isLoggedIn) {
                        when (val screen = currentScreen) {
                            Screen.ProductList -> {
                                ProductListScreen(
                                    onAddProduct = { currentScreen = Screen.AddProduct },
                                    onEditProduct = { productId ->
                                        currentScreen = Screen.EditProduct(productId)
                                    }
                                )
                            }

                            Screen.AddProduct -> {
                                AddProductScreen(
                                    onNavigateBack = { currentScreen = Screen.ProductList }
                                )
                            }

                            is Screen.EditProduct -> {
                                EditProductScreen(
                                    productId = screen.productId,
                                    onNavigateBack = { currentScreen = Screen.ProductList }
                                )
                            }
                        }
                    } else {
                        LoginScreen(
                            onLoginSuccess = { isLoggedIn = true }
                        )
                    }
                }
            }
        }
    }
}

sealed class Screen {
    object ProductList : Screen()
    object AddProduct : Screen()
    data class EditProduct(val productId: String) : Screen()
} 