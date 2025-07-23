package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.PurchaseDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.data.domain.repository.PurchaseRepository
import com.example.ministore.domain.model.Purchase
import com.example.ministore.domain.model.PurchaseItem
import com.example.minstore.domain.models.PurchaseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class PurchaseRepositoryImpl(private val purchaseDao: PurchaseDao) : PurchaseRepository {

    override suspend fun insertPurchaseWithItems(purchase: Purchase, items: List<PurchaseItem>) {
        purchaseDao.insertPurchaseWithItems(purchase.toEntity(), items.map { it.toEntity(purchase.id) })
    }

    override suspend fun updatePurchase(purchase: Purchase) {
        purchaseDao.updatePurchase(purchase.toEntity())
    }

    override suspend fun deletePurchaseWithItems(purchase: Purchase) {
        purchaseDao.deletePurchaseWithItems(purchase.toEntity())
    }

    override fun getPurchaseById(id: String): Flow<Purchase?> {
        return purchaseDao.getPurchaseById(id).map { it?.toDomain() }
    }

    override fun getPurchaseItemsByPurchaseId(purchaseId: String): Flow<List<PurchaseItem>> {
        return purchaseDao.getPurchaseItemsByPurchaseId(purchaseId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllPurchases(): Flow<List<Purchase>> {
        return purchaseDao.getAllPurchases().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPurchasesByProvider(providerId: String): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByProvider(providerId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPurchasesByDateRange(startDate: Date, endDate: Date): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPurchasesByStatus(status: PurchaseStatus): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}