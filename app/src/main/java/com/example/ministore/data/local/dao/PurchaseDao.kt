package com.example.ministore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.ministore.data.local.entity.PurchaseEntity
import com.example.ministore.data.local.entity.PurchaseItemEntity
import com.example.minstore.domain.models.PurchaseStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: PurchaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseItems(items: List<PurchaseItemEntity>)

    @Update
    suspend fun updatePurchase(purchase: PurchaseEntity)

    @Delete
    suspend fun deletePurchase(purchase: PurchaseEntity)

    @Query("DELETE FROM purchase_items WHERE purchaseId = :purchaseId")
    suspend fun deletePurchaseItems(purchaseId: String)

    @Query("SELECT * FROM purchases WHERE id = :id")
    fun getPurchaseById(id: String): Flow<PurchaseEntity?>

    @Query("SELECT * FROM purchase_items WHERE purchaseId = :purchaseId")
    fun getPurchaseItemsByPurchaseId(purchaseId: String): Flow<List<PurchaseItemEntity>>

    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchases WHERE providerId = :providerId ORDER BY date DESC")
    fun getPurchasesByProvider(providerId: String): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchases WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getPurchasesByDateRange(startDate: Date, endDate: Date): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchases WHERE status = :status ORDER BY date DESC")
    fun getPurchasesByStatus(status: PurchaseStatus): Flow<List<PurchaseEntity>>

    @Transaction
    suspend fun insertPurchaseWithItems(purchase: PurchaseEntity, items: List<PurchaseItemEntity>) {
        insertPurchase(purchase)
        insertPurchaseItems(items)
    }

    @Transaction
    suspend fun deletePurchaseWithItems(purchase: PurchaseEntity) {
        deletePurchaseItems(purchase.id)
        deletePurchase(purchase)
    }
} 