package com.ministore.presentation.sale

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ministore.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordSaleDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("1") }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Record Sale",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Product: ${product.name}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Available: ${product.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Price: $${product.price}",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        showError = false
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError) {
                    Text(
                        text = "Please enter a valid quantity",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val quantityNum = quantity.toIntOrNull() ?: 0
                            if (quantityNum > 0 && quantityNum <= product.quantity) {
                                onConfirm(quantityNum)
                            } else {
                                showError = true
                            }
                        }
                    ) {
                        Text("Record Sale")
                    }
                }
            }
        }
    }
} 