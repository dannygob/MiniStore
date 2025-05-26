package com.ministore.domain.repository

import com.ministore.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getSales(): Flow<List<Sale>>
    suspend fun addSale(sale: Sale): Result<Unit>
    suspend fun getSalesByProduct(productId: String): Flow<List<Sale>>
    suspend fun getTotalSales(): Flow<Double>
} 