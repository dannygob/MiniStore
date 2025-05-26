package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.SaleDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Sale
import com.example.ministore.domain.repository.SaleRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepositoryImpl @Inject constructor(
    private val saleDao: SaleDao,
    private val firestore: FirebaseFirestore
) : SaleRepository {

    override fun getAllSales(): Flow<List<Sale>> {
        return saleDao.getAllSales().map { sales ->
            sales.map { it.toDomain() }
        }
    }

    override suspend fun getSaleById(id: Long): Sale? {
        return saleDao.getSaleById(id)?.toDomain()
    }

    override fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>> {
        return saleDao.getSalesByDateRange(startDate, endDate).map { sales ->
            sales.map { it.toDomain() }
        }
    }

    override fun getSalesByStatus(status: String): Flow<List<Sale>> {
        return saleDao.getSalesByStatus(status).map { sales ->
            sales.map { it.toDomain() }
        }
    }

    override suspend fun insertSale(sale: Sale): Long {
        val saleEntity = sale.toEntity()
        val saleItems = sale.items.map { it.toEntity(saleEntity.id) }

        // Insert into local database
        val id = saleDao.insertSale(saleEntity)
        saleDao.insertSaleItems(saleItems)

        // Sync with Firebase
        firestore.collection("sales")
            .document(id.toString())
            .set(sale)

        return id
    }

    override suspend fun updateSale(sale: Sale) {
        val saleEntity = sale.toEntity()
        val saleItems = sale.items.map { it.toEntity(saleEntity.id) }

        // Update local database
        saleDao.updateSale(saleEntity)
        saleDao.insertSaleItems(saleItems)

        // Sync with Firebase
        firestore.collection("sales")
            .document(sale.id.toString())
            .set(sale)
    }

    override suspend fun deleteSale(id: Long) {
        // Soft delete in local database
        saleDao.deleteSale(id)

        // Sync with Firebase
        firestore.collection("sales")
            .document(id.toString())
            .delete()
    }

    override fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Flow<Double?> {
        return saleDao.getTotalSalesByDateRange(startDate, endDate)
    }

    override fun getRecentSales(startDate: Date, endDate: Date, limit: Int): Flow<List<Sale>> {
        return saleDao.getRecentSales(startDate, endDate, limit).map { sales ->
            sales.map { it.toDomain() }
        }
    }
} 