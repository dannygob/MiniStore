package com.example.ministore.data.local.dao

import androidx.room.*
import com.example.ministore.data.local.entity.SaleEntity
import com.example.ministore.data.local.entity.SaleItemEntity
import com.example.ministore.data.local.entity.SaleWithItems
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SaleDao {
    @Transaction
    @Query("SELECT * FROM sales WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllSales(): Flow<List<SaleWithItems>>

    @Transaction
    @Query("SELECT * FROM sales WHERE id = :id AND isDeleted = 0")
    suspend fun getSaleById(id: Long): SaleWithItems?

    @Transaction
    @Query("SELECT * FROM sales WHERE createdAt >= :startDate AND createdAt <= :endDate AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<SaleWithItems>>

    @Transaction
    @Query("SELECT * FROM sales WHERE status = :status AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getSalesByStatus(status: String): Flow<List<SaleWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Transaction
    suspend fun insertSaleWithItems(sale: SaleEntity, items: List<SaleItemEntity>) {
        val saleId = insertSale(sale)
        insertSaleItems(items.map { it.copy(saleId = saleId) })
    }

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Query("UPDATE sales SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteSale(id: Long)

    @Query("SELECT SUM(total) FROM sales WHERE createdAt >= :startDate AND createdAt <= :endDate AND isDeleted = 0")
    fun getTotalSalesByDateRange(startDate: Date, endDate: Date): Flow<Double?>

    @Query("SELECT * FROM sales WHERE createdAt >= :startDate AND createdAt <= :endDate AND isDeleted = 0 ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSales(startDate: Date, endDate: Date, limit: Int): Flow<List<SaleWithItems>>
} 