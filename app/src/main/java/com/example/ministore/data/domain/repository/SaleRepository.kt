YEAH
package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Sale
import com.example.ministore.domain.model.SaleItem
import com.example.ministore.domain.model.SaleStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>
    suspend fun getSaleById(id: Long): Sale?
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>
    fun getSalesByStatus(status: SaleStatus): Flow<List<Sale>>
    suspend fun insertSaleWithItems(sale: Sale, items: List<SaleItem>): Long
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(id: Long)
    fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Flow<Double?>
    fun getRecentSales(startDate: Date, endDate: Date, limit: Int): Flow<List<Sale>>
}