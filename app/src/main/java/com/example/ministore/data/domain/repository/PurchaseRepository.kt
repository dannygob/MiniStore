package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Purchase
import com.example.ministore.domain.model.PurchaseItem
import com.example.minstore.domain.models.PurchaseStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PurchaseRepository {
    suspend fun insertPurchaseWithItems(purchase: Purchase, items: List<PurchaseItem>)
    suspend fun updatePurchase(purchase: Purchase)
    suspend fun deletePurchaseWithItems(purchase: Purchase)
    fun getPurchaseById(id: String): Flow<Purchase?>
    fun getPurchaseItemsByPurchaseId(purchaseId: String): Flow<List<PurchaseItem>>
    fun getAllPurchases(): Flow<List<Purchase>>
    fun getPurchasesByProvider(providerId: String): Flow<List<Purchase>>
    fun getPurchasesByDateRange(startDate: Date, endDate: Date): Flow<List<Purchase>>
    fun getPurchasesByStatus(status: PurchaseStatus): Flow<List<Purchase>>
}