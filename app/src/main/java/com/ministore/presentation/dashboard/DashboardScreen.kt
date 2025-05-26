package com.ministore.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ministore.domain.model.Product
import com.ministore.domain.model.Sale
import com.ministore.presentation.product.ProductViewModel
import com.ministore.presentation.sale.SaleViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit,
    productViewModel: ProductViewModel = hiltViewModel(),
    saleViewModel: SaleViewModel = hiltViewModel()
) {
    val productState by productViewModel.uiState.collectAsState()
    val saleState by saleViewModel.uiState.collectAsState()
    val totalSales by saleViewModel.totalSales.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToProducts) {
                        Icon(Icons.Default.Inventory, "Products")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard(
                        title = "Total Sales",
                        value = formatCurrency(totalSales),
                        icon = Icons.Default.AttachMoney,
                        modifier = Modifier.weight(1f)
                    )

                    val products = if (productState is ProductViewModel.ProductUiState.Success) {
                        (productState as ProductViewModel.ProductUiState.Success).products
                    } else emptyList()

                    SummaryCard(
                        title = "Low Stock",
                        value = products.count { it.quantity <= Product.DEFAULT_MIN_STOCK }
                            .toString(),
                        icon = Icons.Default.Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Sales
            item {
                Text(
                    "Recent Sales",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (saleState is SaleViewModel.SaleUiState.Success) {
                val sales = (saleState as SaleViewModel.SaleUiState.Success).sales
                items(sales.take(5)) { sale ->
                    SaleItem(sale = sale)
                }
            }

            // Low Stock Products
            item {
                Text(
                    "Low Stock Products",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (productState is ProductViewModel.ProductUiState.Success) {
                val products = (productState as ProductViewModel.ProductUiState.Success).products
                items(products.filter { it.quantity <= Product.DEFAULT_MIN_STOCK }) { product ->
                    LowStockItem(product = product)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SaleItem(sale: Sale) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = sale.productName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatDate(sale.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatCurrency(sale.totalAmount),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Qty: ${sale.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun LowStockItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Category: ${product.category}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Stock: ${product.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = formatCurrency(product.price),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance().format(amount)
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 