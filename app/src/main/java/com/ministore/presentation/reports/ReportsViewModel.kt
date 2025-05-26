package com.ministore.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.usecase.sales.SalesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val salesUseCases: SalesUseCases
) : ViewModel() {

    private val _reportsState = MutableStateFlow(ReportsState())
    val reportsState: StateFlow<ReportsState> = _reportsState.asStateFlow()

    private var currentPeriod = ReportPeriod.DAILY

    init {
        loadReports()
    }

    fun updatePeriod(period: ReportPeriod) {
        currentPeriod = period
        loadReports()
    }

    fun refreshReports() {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            try {
                val startDate = getStartDate(currentPeriod)
                val endDate = System.currentTimeMillis()

                // Load sales data
                salesUseCases.getSalesForPeriod(startDate, endDate)
                    .collect { sales ->
                        val totalSales = sales.sumOf { it.total }
                        val totalOrders = sales.size

                        // Calculate top products
                        val productSales = mutableMapOf<String, TopProduct>()
                        sales.forEach { sale ->
                            sale.items.forEach { item ->
                                productSales.merge(
                                    item.productName,
                                    TopProduct(
                                        name = item.productName,
                                        quantitySold = item.quantity,
                                        revenue = item.price * item.quantity
                                    )
                                ) { existing, new ->
                                    existing.copy(
                                        quantitySold = existing.quantitySold + new.quantitySold,
                                        revenue = existing.revenue + new.revenue
                                    )
                                }
                            }
                        }

                        val topProducts =
                            productSales.values.sortedByDescending { it.revenue }.take(5)

                        // Create sale summaries
                        val recentSales = sales
                            .sortedByDescending { it.timestamp }
                            .take(10)
                            .map { sale ->
                                SaleSummary(
                                    id = sale.id,
                                    total = sale.total,
                                    itemCount = sale.items.sumOf { it.quantity },
                                    timestamp = sale.timestamp
                                )
                            }

                        _reportsState.value = ReportsState(
                            totalSales = totalSales,
                            totalOrders = totalOrders,
                            topProducts = topProducts,
                            recentSales = recentSales
                        )
                    }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun getStartDate(period: ReportPeriod): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        when (period) {
            ReportPeriod.DAILY -> calendar.add(Calendar.DAY_OF_MONTH, -1)
            ReportPeriod.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
            ReportPeriod.MONTHLY -> calendar.add(Calendar.MONTH, -1)
            ReportPeriod.YEARLY -> calendar.add(Calendar.YEAR, -1)
        }

        return calendar.timeInMillis
    }
}

data class ReportsState(
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val topProducts: List<TopProduct> = emptyList(),
    val recentSales: List<SaleSummary> = emptyList()
)

data class TopProduct(
    val name: String,
    val quantitySold: Int,
    val revenue: Double
)

data class SaleSummary(
    val id: String,
    val total: Double,
    val itemCount: Int,
    val timestamp: Long
)

enum class ReportPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
} 