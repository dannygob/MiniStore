package com.ministore.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.model.Product
import com.ministore.domain.usecase.product.ProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortBy = MutableStateFlow<String?>(null)

    @OptIn(FlowPreview::class)
    val products = combine(
        _searchQuery.debounce(300),
        _sortBy
    ) { query, sortBy ->
        Pair(query, sortBy)
    }.flatMapLatest { (query, sortBy) ->
        if (query.isBlank()) {
            productUseCases.getProducts(sortBy)
        } else {
            productUseCases.searchProducts(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun searchProducts(query: String) {
        _searchQuery.value = query
    }

    fun sortProducts(sortBy: String) {
        _sortBy.value = sortBy
    }

    fun updateStock(productId: Long, quantityChange: Int) {
        viewModelScope.launch {
            try {
                productUseCases.updateStock(productId, quantityChange)
            } catch (e: Exception) {
                // Handle error (you might want to add error handling through a state)
            }
        }
    }
} 