package com.ministore.data.local.dao

import androidx.room.*
import com.ministore.data.local.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: String): SaleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSales(sales: List<SaleEntity>)

    @Query("SELECT * FROM sales WHERE syncedToCloud = 0")
    suspend fun getUnsyncedSales(): List<SaleEntity>

    @Query("UPDATE sales SET syncedToCloud = 1 WHERE id = :saleId")
    suspend fun markSaleAsSynced(saleId: String)

    @Query("SELECT * FROM sales WHERE productId = :productId ORDER BY timestamp DESC")
    fun getSalesByProduct(productId: String): Flow<List<SaleEntity>>

    @Query("SELECT SUM(totalAmount) FROM sales")
    fun getTotalSales(): Flow<Double?>

    @Query("DELETE FROM sales")
    suspend fun deleteAllSales()
} 