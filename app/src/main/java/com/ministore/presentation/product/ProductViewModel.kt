package com.ministore.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.model.Product
import com.ministore.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts()
                .catch { error ->
                    _uiState.value = ProductUiState.Error(error.message ?: "Unknown error")
                }
                .collect { products ->
                    _uiState.value = ProductUiState.Success(products)
                }
        }
    }

    fun getProduct(id: String): Flow<Product?> {
        return repository.getProduct(id)
            .catch { error ->
                _uiState.value = ProductUiState.Error(error.message ?: "Failed to load product")
                emit(null)
            }
    }

    fun addProduct(
        name: String,
        price: Double,
        quantity: Int,
        barcode: String = "",
        category: String = ""
    ) {
        viewModelScope.launch {
            val product = Product(
                name = name,
                price = price,
                quantity = quantity,
                barcode = barcode,
                category = category
            )
            repository.addProduct(product)
                .onFailure { error ->
                    _uiState.value = ProductUiState.Error(error.message ?: "Failed to add product")
                }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
                .onFailure { error ->
                    _uiState.value =
                        ProductUiState.Error(error.message ?: "Failed to update product")
                }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            repository.deleteProduct(id)
                .onFailure { error ->
                    _uiState.value =
                        ProductUiState.Error(error.message ?: "Failed to delete product")
                }
        }
    }

    fun updateQuantity(id: String, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateProductQuantity(id, newQuantity)
                .onFailure { error ->
                    _uiState.value =
                        ProductUiState.Error(error.message ?: "Failed to update quantity")
                }
        }
    }
}

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
} 