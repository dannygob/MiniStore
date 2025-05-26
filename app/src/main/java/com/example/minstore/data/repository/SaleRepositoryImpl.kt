package com.example.minstore.data.repository

import com.example.minstore.data.local.dao.SaleDao
import com.example.minstore.data.local.entities.SaleDetailEntity
import com.example.minstore.data.local.entities.SaleEntity
import com.example.minstore.domain.models.Sale
import com.example.minstore.domain.models.SaleDetail
import com.example.minstore.domain.models.SaleStatus
import com.example.minstore.domain.repository.SaleRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val saleDao: SaleDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : SaleRepository {

    private val salesCollection = firestore.collection("sales")
    private val storageRef = storage.reference.child("sale_receipts")

    override fun getAllSales(): Flow<List<Sale>> =
        saleDao.getAllSales().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getSaleById(id: String): Sale? {
        val saleEntity = saleDao.getSaleById(id) ?: return null
        val details = saleDao.getSaleDetails(id)
        return saleEntity.toDomain(details)
    }

    override fun getSalesByClient(clientId: String): Flow<List<Sale>> =
        saleDao.getSalesByClient(clientId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getSalesByStatus(status: SaleStatus): Flow<List<Sale>> =
        saleDao.getSalesByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>> =
        saleDao.getSalesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertSale(sale: Sale) {
        val saleEntity = sale.toEntity()
        val detailEntities = sale.items.map { it.toEntity(sale.id) }
        saleDao.insertSaleWithDetails(saleEntity, detailEntities)
        salesCollection.document(sale.id).set(sale).await()
    }

    override suspend fun updateSale(sale: Sale) {
        val saleEntity = sale.toEntity()
        val detailEntities = sale.items.map { it.toEntity(sale.id) }
        saleDao.insertSaleWithDetails(saleEntity, detailEntities)
        salesCollection.document(sale.id).set(sale).await()
    }

    override suspend fun deleteSale(sale: Sale) {
        saleDao.deleteSale(sale.toEntity())
        salesCollection.document(sale.id).delete().await()
    }

    override fun getTotalSalesInRange(startDate: Date, endDate: Date): Flow<Double?> =
        saleDao.getTotalSalesInRange(startDate, endDate)

    override fun getSalesCountInRange(startDate: Date, endDate: Date): Flow<Int> =
        saleDao.getSalesCountInRange(startDate, endDate)

    override suspend fun syncWithFirestore() {
        val remoteSales = salesCollection.get().await()
            .documents.mapNotNull { it.toObject(Sale::class.java) }
        remoteSales.forEach { sale ->
            val saleEntity = sale.toEntity()
            val detailEntities = sale.items.map { it.toEntity(sale.id) }
            saleDao.insertSaleWithDetails(saleEntity, detailEntities)
        }
    }

    override suspend fun generateSaleReceipt(sale: Sale): String {
        // Here you would implement the logic to generate a PDF receipt
        // For now, we'll just store a placeholder file
        val receiptRef = storageRef.child("${sale.id}/${UUID.randomUUID()}.pdf")
        val bytes = ByteArray(0) // Placeholder for actual PDF content
        receiptRef.putBytes(bytes).await()
        return receiptRef.downloadUrl.await().toString()
    }

    private fun SaleEntity.toDomain(details: List<SaleDetailEntity> = emptyList()) = Sale(
        id = id,
        clientId = clientId,
        total = total,
        paymentMethod = paymentMethod,
        items = details.map { it.toDomain() },
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun SaleDetailEntity.toDomain() = SaleDetail(
        id = id,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal
    )

    private fun Sale.toEntity() = SaleEntity(
        id = id,
        clientId = clientId,
        total = total,
        paymentMethod = paymentMethod,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun SaleDetail.toEntity(saleId: String) = SaleDetailEntity(
        id = id,
        saleId = saleId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal
    )
} 