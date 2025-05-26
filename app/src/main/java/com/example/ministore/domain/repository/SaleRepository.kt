package com.example.ministore.domain.repository

import com.example.ministore.domain.model.Sale
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>

    suspend fun getSaleById(id: Long): Sale?

    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>

    fun getSalesByStatus(status: String): Flow<List<Sale>>

    suspend fun insertSale(sale: Sale): Long

    suspend fun updateSale(sale: Sale)

    suspend fun deleteSale(id: Long)

    fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Flow<Double?>

    fun getRecentSales(startDate: Date, endDate: Date, limit: Int): Flow<List<Sale>>
} 