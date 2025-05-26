package com.ministore.presentation.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.model.Product
import com.ministore.domain.usecase.product.ProductUseCases
import com.ministore.domain.usecase.sales.SalesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val salesUseCases: SalesUseCases
) : ViewModel() {

    private val _salesState = MutableStateFlow(SalesState())
    val salesState: StateFlow<SalesState> = _salesState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    fun searchProducts(query: String) {
        viewModelScope.launch {
            productUseCases.searchProducts(query)
                .collect { products ->
                    _searchResults.value = products
                }
        }
    }

    fun addItem(product: Product) {
        val currentItems = _salesState.value.items.toMutableList()
        val existingItem = currentItems.find { it.productId == product.id }

        if (existingItem != null) {
            // Update quantity if item already exists
            updateItemQuantity(product.id, existingItem.quantity + 1)
        } else {
            // Add new item
            currentItems.add(
                SaleItem(
                    productId = product.id,
                    productName = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
            updateSalesState(currentItems)
        }
    }

    fun updateItemQuantity(productId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(productId)
            return
        }

        val currentItems = _salesState.value.items.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.productId == productId }

        if (itemIndex != -1) {
            currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = newQuantity)
            updateSalesState(currentItems)
        }
    }

    fun removeItem(productId: Long) {
        val currentItems = _salesState.value.items.toMutableList()
        currentItems.removeAll { it.productId == productId }
        updateSalesState(currentItems)
    }

    private fun updateSalesState(items: List<SaleItem>) {
        val total = items.sumOf { it.price * it.quantity }
        _salesState.value = _salesState.value.copy(
            items = items,
            total = total
        )
    }

    fun completeSale(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            try {
                val sale = Sale(
                    id = UUID.randomUUID().toString(),
                    items = _salesState.value.items,
                    total = _salesState.value.total,
                    paymentMethod = paymentMethod,
                    timestamp = System.currentTimeMillis()
                )

                salesUseCases.createSale(sale)

                // Update product stock
                _salesState.value.items.forEach { item ->
                    productUseCases.updateStock(
                        productId = item.productId,
                        quantityChange = -item.quantity
                    )
                }

                // Clear cart
                _salesState.value = SalesState()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class SalesState(
    val items: List<SaleItem> = emptyList(),
    val total: Double = 0.0
)

data class SaleItem(
    val productId: Long,
    val productName: String,
    val price: Double,
    val quantity: Int
)

data class Sale(
    val id: String,
    val items: List<SaleItem>,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val timestamp: Long
)

enum class PaymentMethod {
    CASH,
    CARD
} 