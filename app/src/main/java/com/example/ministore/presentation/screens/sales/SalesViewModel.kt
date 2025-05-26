package com.example.ministore.presentation.screens.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ministore.domain.model.PaymentMethod
import com.example.ministore.domain.model.Sale
import com.example.ministore.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

data class SalesState(
    val sales: List<Sale> = emptyList(),
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val lastDeletedSale: Sale? = null
) {
    val formattedTotalSales: String
        get() = formatCurrency(totalSales)

    fun formatCurrency(amount: Double): String =
        NumberFormat.getCurrencyInstance().format(amount)
}

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SalesState())
    val state: StateFlow<SalesState> = _state.asStateFlow()

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    init {
        loadSales()
    }

    private fun loadSales() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                // Get today's start and end
                val calendar = Calendar.getInstance()
                calendar.time = _selectedDate.value
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startDate = calendar.time

                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endDate = calendar.time

                // Collect sales for the selected date
                saleRepository.getSalesByDateRange(startDate, endDate)
                    .combine(
                        saleRepository.getTotalSalesByDateRange(
                            startDate,
                            endDate
                        )
                    ) { sales, total ->
                        // Filter by payment method if selected
                        val filteredSales = _state.value.selectedPaymentMethod?.let { method ->
                            sales.filter { it.paymentMethod == method }
                        } ?: sales

                        // Calculate totals for filtered sales
                        val filteredTotal = filteredSales.sumOf { it.total }

                        _state.update {
                            it.copy(
                                sales = filteredSales,
                                totalSales = filteredTotal,
                                totalOrders = filteredSales.size,
                                isLoading = false,
                                error = null
                            )
                        }
                    }.collect()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun onDateSelected(date: Date) {
        _selectedDate.value = date
        loadSales()
    }

    fun onPaymentMethodSelected(method: PaymentMethod?) {
        _state.update { it.copy(selectedPaymentMethod = method) }
        loadSales()
    }

    fun deleteSale(id: Long) {
        viewModelScope.launch {
            try {
                // Store the sale before deletion for undo
                val saleToDelete = state.value.sales.find { it.id == id }
                saleToDelete?.let { sale ->
                    saleRepository.deleteSale(id)
                    _state.update { it.copy(lastDeletedSale = sale) }
                    // Reload sales after deletion
                    loadSales()
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun undoDelete(sale: Sale) {
        viewModelScope.launch {
            try {
                saleRepository.insertSale(sale)
                _state.update { it.copy(lastDeletedSale = null) }
                // Reload sales after restoration
                loadSales()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
} 