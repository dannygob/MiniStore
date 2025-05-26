package com.ministore.presentation.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.usecase.product.ProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Load low stock products
            productUseCases.getLowStockProducts()
                .collect { products ->
                    _dashboardState.update { state ->
                        state.copy(
                            lowStockCount = products.size,
                            lowStockItems = products.map { product ->
                                LowStockItem(
                                    id = product.id,
                                    name = product.name,
                                    currentStock = product.stockQuantity,
                                    minStock = product.minStockLevel
                                )
                            }
                        )
                    }
                }
        }

        // Simulate loading recent activities (replace with actual data source)
        val recentActivities = listOf(
            DashboardActivity(
                icon = Icons.Default.ShoppingCart,
                description = "New sale completed",
                time = "2 min ago"
            ),
            DashboardActivity(
                icon = Icons.Default.Inventory,
                description = "Stock updated for Product X",
                time = "15 min ago"
            ),
            DashboardActivity(
                icon = Icons.Default.Person,
                description = "New customer registered",
                time = "1 hour ago"
            )
        )

        _dashboardState.update { state ->
            state.copy(
                todaySales = BigDecimal("1234.56"),
                recentActivities = recentActivities
            )
        }
    }

    fun restockItem(productId: Long) {
        viewModelScope.launch {
            try {
                productUseCases.updateStock(productId, 10) // Add 10 units as default
                loadDashboardData() // Refresh dashboard data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class DashboardState(
    val todaySales: BigDecimal = BigDecimal.ZERO,
    val lowStockCount: Int = 0,
    val lowStockItems: List<LowStockItem> = emptyList(),
    val recentActivities: List<DashboardActivity> = emptyList()
)

data class LowStockItem(
    val id: Long,
    val name: String,
    val currentStock: Int,
    val minStock: Int
)

data class DashboardActivity(
    val icon: ImageVector,
    val description: String,
    val time: String
) 