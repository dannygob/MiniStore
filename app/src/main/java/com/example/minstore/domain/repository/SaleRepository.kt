package com.example.minstore.domain.repository

import com.example.minstore.domain.models.Sale
import com.example.minstore.domain.models.SaleStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>
    suspend fun getSaleById(id: String): Sale?
    fun getSalesByClient(clientId: String): Flow<List<Sale>>
    fun getSalesByStatus(status: SaleStatus): Flow<List<Sale>>
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>
    suspend fun insertSale(sale: Sale)
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(sale: Sale)
    fun getTotalSalesInRange(startDate: Date, endDate: Date): Flow<Double?>
    fun getSalesCountInRange(startDate: Date, endDate: Date): Flow<Int>

    // Firebase specific operations
    suspend fun syncWithFirestore()
    suspend fun generateSaleReceipt(sale: Sale): String
} 