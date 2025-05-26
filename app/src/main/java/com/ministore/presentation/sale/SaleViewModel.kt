package com.ministore.presentation.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.model.Product
import com.ministore.domain.model.Sale
import com.ministore.domain.repository.ProductRepository
import com.ministore.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SaleUiState>(SaleUiState.Loading)
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    private val _totalSales = MutableStateFlow(0.0)
    val totalSales: StateFlow<Double> = _totalSales.asStateFlow()

    init {
        loadSales()
        observeTotalSales()
    }

    private fun loadSales() {
        viewModelScope.launch {
            saleRepository.getSales()
                .catch { error ->
                    _uiState.value = SaleUiState.Error(error.message ?: "Unknown error")
                }
                .collect { sales ->
                    _uiState.value = SaleUiState.Success(sales)
                }
        }
    }

    private fun observeTotalSales() {
        viewModelScope.launch {
            saleRepository.getTotalSales()
                .catch { /* Handle error if needed */ }
                .collect { total ->
                    _totalSales.value = total
                }
        }
    }

    fun recordSale(product: Product, quantity: Int) {
        viewModelScope.launch {
            val sale = Sale(
                productId = product.id,
                productName = product.name,
                quantity = quantity,
                pricePerUnit = product.price,
                totalAmount = product.price * quantity
            )

            saleRepository.addSale(sale)
                .onSuccess {
                    // Update product quantity
                    val newQuantity = product.quantity - quantity
                    if (newQuantity >= 0) {
                        productRepository.updateProductQuantity(product.id, newQuantity)
                    }
                }
                .onFailure { error ->
                    _uiState.value = SaleUiState.Error(error.message ?: "Failed to record sale")
                }
        }
    }
}

sealed class SaleUiState {
    object Loading : SaleUiState()
    data class Success(val sales: List<Sale>) : SaleUiState()
    data class Error(val message: String) : SaleUiState()
} 