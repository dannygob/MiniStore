package com.example.ministore.presentation.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.domain.model.Product
import com.example.ministore.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state: StateFlow<InventoryState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        productRepository.getAllProducts()
                    } else {
                        productRepository.searchProducts(query)
                    }
                }
                .catch { e ->
                    _state.update { it.copy(error = e.message) }
                }
                .collect { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun refreshProducts() {
        _state.update { it.copy(isLoading = true) }
        onSearchQueryChange(_searchQuery.value)
    }
} 