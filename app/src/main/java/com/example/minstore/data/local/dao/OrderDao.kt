package com.example.minstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.minstore.data.local.entities.OrderDetailEntity
import com.example.minstore.data.local.entities.OrderEntity
import com.example.minstore.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getOrdersByClient(clientId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE deliveryDate BETWEEN :startDate AND :endDate ORDER BY deliveryDate ASC")
    fun getOrdersByDeliveryDateRange(startDate: Date, endDate: Date): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderDetails(details: List<OrderDetailEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    suspend fun getOrderDetails(orderId: String): List<OrderDetailEntity>

    @Transaction
    suspend fun insertOrderWithDetails(order: OrderEntity, details: List<OrderDetailEntity>) {
        insertOrder(order)
        insertOrderDetails(details)
    }

    @Query(
        """
        SELECT COUNT(*) FROM orders 
        WHERE status = :status 
        AND deliveryDate BETWEEN :startDate AND :endDate
    """
    )
    fun getOrdersCountByStatusInRange(
        status: OrderStatus,
        startDate: Date,
        endDate: Date
    ): Flow<Int>

    @Query(
        """
        SELECT * FROM orders 
        WHERE status = 'PENDING' 
        OR status = 'IN_PROGRESS' 
        ORDER BY deliveryDate ASC
    """
    )
    fun getActiveOrders(): Flow<List<OrderEntity>>
} 