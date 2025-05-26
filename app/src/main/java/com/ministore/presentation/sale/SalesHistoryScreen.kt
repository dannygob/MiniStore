package com.ministore.presentation.sale

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ministore.domain.model.Sale
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesHistoryScreen(
    viewModel: SaleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalSales by viewModel.totalSales.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var sortOrder by remember { mutableStateOf(SortOrder.NEWEST) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales History") },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Sales",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatCurrency(totalSales),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Filters
            if (showFilters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sort by:", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        FilterChip(
                            selected = sortOrder == SortOrder.NEWEST,
                            onClick = { sortOrder = SortOrder.NEWEST },
                            label = { Text("Newest") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = sortOrder == SortOrder.OLDEST,
                            onClick = { sortOrder = SortOrder.OLDEST },
                            label = { Text("Oldest") }
                        )
                    }
                }
            }

            when (val state = uiState) {
                is SaleUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SaleUiState.Success -> {
                    val sales = when (sortOrder) {
                        SortOrder.NEWEST -> state.sales.sortedByDescending { it.timestamp }
                        SortOrder.OLDEST -> state.sales.sortedBy { it.timestamp }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sales) { sale ->
                            SaleHistoryItem(sale = sale)
                        }
                    }
                }

                is SaleUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SaleHistoryItem(sale: Sale) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sale.productName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatCurrency(sale.totalAmount),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDate(sale.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Qty: ${sale.quantity} @ ${formatCurrency(sale.pricePerUnit)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private enum class SortOrder {
    NEWEST,
    OLDEST
}

private fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance().format(amount)
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 