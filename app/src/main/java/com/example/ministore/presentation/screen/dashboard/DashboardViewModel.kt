package com.example.ministore.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.domain.model.Product
import com.example.ministore.domain.model.Sale
import com.example.ministore.domain.repository.ProductRepository
import com.example.ministore.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Get today's date range
            val today = Calendar.getInstance()
            val startOfDay = today.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.time
            val endOfDay = today.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.time

            // Load low stock products
            productRepository.getLowStockProducts()
                .onEach { products ->
                    _state.update { it.copy(lowStockProducts = products) }
                }
                .launchIn(viewModelScope)

            // Load today's sales
            saleRepository.getSalesByDateRange(startOfDay, endOfDay)
                .onEach { sales ->
                    _state.update { it.copy(todaySales = sales) }
                }
                .launchIn(viewModelScope)

            // Load total sales amount
            saleRepository.getTotalSalesInRange(startOfDay, endOfDay)
                .onEach { total ->
                    _state.update { it.copy(todayTotalSales = total ?: 0.0) }
                }
                .launchIn(viewModelScope)

            // Load sales count
            saleRepository.getSalesCountInRange(startOfDay, endOfDay)
                .onEach { count ->
                    _state.update { it.copy(todaySalesCount = count) }
                }
                .launchIn(viewModelScope)
        }
    }

    fun refresh() {
        loadDashboardData()
    }
}

data class DashboardState(
    val lowStockProducts: List<Product> = emptyList(),
    val todaySales: List<Sale> = emptyList(),
    val todayTotalSales: Double = 0.0,
    val todaySalesCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
) 