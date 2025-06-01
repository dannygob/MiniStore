package com.ministore.presentation.product

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Load product data
    LaunchedEffect(productId) {
        viewModel.getProduct(productId).collect { product ->
            product?.let {
                name = it.name
                price = it.price.toString()
                quantity = it.quantity.toString()
                barcode = it.barcode
                category = it.category
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Product") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO: Add input fields to display and edit new Product properties:
            // - purchasePrice
            // - providerId
            // - location (aisle, shelf, level)
            // - minimumStock
            // - expirationDate
            // - imageUrl (display current image, allow changing/removing).
            // Ensure these values are collected and used when updating the Product object
            // which is then passed to ProductViewModel's updateProduct method.
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = showError && name.isBlank()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = showError && price.isBlank()
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = showError && quantity.isBlank()
            )

            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Barcode (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    label = { Text("Category") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    isError = showError && category.isBlank()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Product.VALID_CATEGORIES.forEach { validCategory ->
                        DropdownMenuItem(
                            text = { Text(validCategory) },
                            onClick = {
                                category = validCategory
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank() || quantity.isBlank() || category.isBlank()) {
                        showError = true
                        return@Button
                    }

                    val priceValue = price.toDoubleOrNull()
                    val quantityValue = quantity.toIntOrNull()

                    if (priceValue != null && quantityValue != null) {
                        viewModel.updateProduct(
                            Product(
                                id = productId,
                                name = name,
                                price = priceValue,
                                quantity = quantityValue,
                                barcode = barcode,
                                category = category
                            )
                        )
                        onNavigateBack()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            if (showError) {
                Text(
                    text = "Please fill in all required fields correctly",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
} 