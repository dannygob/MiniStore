package com.ministore.presentation.sales

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ministore.R
import com.ministore.presentation.util.rememberToastManager
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    onNavigateBack: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    val toastManager = rememberToastManager()
    val salesState by viewModel.salesState.collectAsState()
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    var showAddItemDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_sale)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddItemDialog = true }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: ${currencyFormat.format(salesState.total)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = {
                            if (salesState.items.isNotEmpty()) {
                                showPaymentDialog = true
                            } else {
                                toastManager.showWarning("Add items to cart first")
                            }
                        },
                        enabled = salesState.items.isNotEmpty()
                    ) {
                        Text("Checkout")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Cart Items
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(salesState.items) { item ->
                    CartItem(
                        item = item,
                        currencyFormat = currencyFormat,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateItemQuantity(item.productId, newQuantity)
                            toastManager.showInfo("Quantity updated")
                        },
                        onRemove = {
                            viewModel.removeItem(item.productId)
                            toastManager.showInfo("Item removed from cart")
                        }
                    )
                }
            }
        }
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onProductSelected = { product ->
                viewModel.addItem(product)
                toastManager.showSuccess("Item added to cart")
                showAddItemDialog = false
            }
        )
    }

    // Payment Dialog
    if (showPaymentDialog) {
        PaymentDialog(
            total = salesState.total,
            onDismiss = { showPaymentDialog = false },
            onPaymentComplete = { paymentMethod ->
                viewModel.completeSale(paymentMethod)
                toastManager.showSuccess("Sale completed successfully")
                onNavigateBack()
            }
        )
    }
}

@Composable
private fun CartItem(
    item: SaleItem,
    currencyFormat: NumberFormat,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = currencyFormat.format(item.price),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }

                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onQuantityChange(item.quantity + 1) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemDialog(
    onDismiss: () -> Unit,
    onProductSelected: (Product) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Product") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Products") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Product list would go here
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PaymentDialog(
    total: Double,
    onDismiss: () -> Unit,
    onPaymentComplete: (PaymentMethod) -> Unit
) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Payment") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Total: ${currencyFormat.format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Select payment method:")
                PaymentMethodButton(
                    text = "Cash",
                    icon = Icons.Default.Money,
                    onClick = { onPaymentComplete(PaymentMethod.CASH) }
                )
                PaymentMethodButton(
                    text = "Card",
                    icon = Icons.Default.CreditCard,
                    onClick = { onPaymentComplete(PaymentMethod.CARD) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PaymentMethodButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Text(text)
        }
    }
} 