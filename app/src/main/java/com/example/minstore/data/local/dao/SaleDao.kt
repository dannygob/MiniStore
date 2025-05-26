package com.example.minstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.minstore.data.local.entities.SaleDetailEntity
import com.example.minstore.data.local.entities.SaleEntity
import com.example.minstore.domain.models.SaleStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY createdAt DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: String): SaleEntity?

    @Query("SELECT * FROM sales WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getSalesByClient(clientId: String): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE status = :status ORDER BY createdAt DESC")
    fun getSalesByStatus(status: SaleStatus): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<SaleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleDetails(details: List<SaleDetailEntity>)

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Delete
    suspend fun deleteSale(sale: SaleEntity)

    @Query("SELECT * FROM sale_details WHERE saleId = :saleId")
    suspend fun getSaleDetails(saleId: String): List<SaleDetailEntity>

    @Transaction
    suspend fun insertSaleWithDetails(sale: SaleEntity, details: List<SaleDetailEntity>) {
        insertSale(sale)
        insertSaleDetails(details)
    }

    @Query(
        """
        SELECT SUM(total) FROM sales 
        WHERE createdAt BETWEEN :startDate AND :endDate 
        AND status = 'COMPLETED'
    """
    )
    fun getTotalSalesInRange(startDate: Date, endDate: Date): Flow<Double?>

    @Query(
        """
        SELECT COUNT(*) FROM sales 
        WHERE createdAt BETWEEN :startDate AND :endDate 
        AND status = 'COMPLETED'
    """
    )
    fun getSalesCountInRange(startDate: Date, endDate: Date): Flow<Int>
} 