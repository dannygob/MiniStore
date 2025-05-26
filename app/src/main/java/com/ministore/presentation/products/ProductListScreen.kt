package com.ministore.presentation.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ministore.R
import com.ministore.domain.model.Product
import com.ministore.presentation.util.rememberToastManager
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToProductDetail: (Long) -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val toastManager = rememberToastManager()
    var showSortMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_products)) },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddProduct()
                    toastManager.showShort(R.string.add_product)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchProducts(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(stringResource(R.string.search)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Products list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onProductClick = {
                            onNavigateToProductDetail(product.id)
                            toastManager.showShort("Selected: ${product.name}")
                        },
                        onStockUpdate = { quantity ->
                            viewModel.updateStock(product.id, quantity)
                            toastManager.showShort(R.string.toast_stock_updated)
                        }
                    )
                }
            }
        }

        // Sort menu
        DropdownMenu(
            expanded = showSortMenu,
            onDismissRequest = { showSortMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Name") },
                onClick = {
                    viewModel.sortProducts("name")
                    toastManager.showShort("Sorted by name")
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Category") },
                onClick = {
                    viewModel.sortProducts("category")
                    toastManager.showShort("Sorted by category")
                    showSortMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Stock") },
                onClick = {
                    viewModel.sortProducts("stock")
                    toastManager.showShort("Sorted by stock level")
                    showSortMenu = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductItem(
    product: Product,
    onProductClick: () -> Unit,
    onStockUpdate: (Int) -> Unit
) {
    val toastManager = rememberToastManager()
    var showStockDialog by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = currencyFormat.format(product.price),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stock: ${product.stockQuantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = { showStockDialog = true }
                ) {
                    Text("Update Stock")
                }
            }

            if (product.needsRestocking()) {
                Text(
                    text = "Low Stock Alert!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    if (showStockDialog) {
        var stockQuantity by remember { mutableStateOf(product.stockQuantity.toString()) }

        AlertDialog(
            onDismissRequest = { showStockDialog = false },
            title = { Text("Update Stock") },
            text = {
                OutlinedTextField(
                    value = stockQuantity,
                    onValueChange = { stockQuantity = it },
                    label = { Text("Quantity") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        stockQuantity.toIntOrNull()?.let { quantity ->
                            onStockUpdate(quantity - product.stockQuantity)
                            if (quantity < product.minStockLevel) {
                                toastManager.showLong(
                                    context.getString(
                                        R.string.toast_low_stock_alert,
                                        product.name
                                    )
                                )
                            }
                        }
                        showStockDialog = false
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showStockDialog = false
                        toastManager.showShort(R.string.toast_operation_cancelled)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
} 