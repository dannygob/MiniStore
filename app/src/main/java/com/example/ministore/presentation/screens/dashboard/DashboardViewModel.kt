package com.example.ministore.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.domain.model.Product
import com.example.ministore.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DashboardState(
    val todaySales: Double = 0.0,
    val lowStockCount: Int = 0,
    val totalProducts: Int = 0,
    val pendingOrders: Int = 0,
    val lowStockProducts: List<Product> = emptyList(),
    val recentSales: List<Any> = emptyList() // TODO: Replace with Sale model
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Collect low stock products
            productRepository.getLowStockProducts()
                .collect { products ->
                    _state.update { currentState ->
                        currentState.copy(
                            lowStockProducts = products,
                            lowStockCount = products.size
                        )
                    }
                }
        }

        viewModelScope.launch {
            // Collect total products
            productRepository.getAllProducts()
                .collect { products ->
                    _state.update { currentState ->
                        currentState.copy(
                            totalProducts = products.size
                        )
                    }
                }
        }

        // TODO: Implement sales and orders collection when repositories are available
    }
} 