package com.example.ministore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.ministore.data.local.entity.OrderEntity
import com.example.ministore.data.local.entity.OrderItemEntity
import com.example.ministore.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItems(orderId: String)

    @Query("SELECT * FROM orders WHERE id = :id")
    fun getOrderById(id: String): Flow<OrderEntity?>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderId(orderId: String): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE clientId = :clientId ORDER BY orderDate DESC")
    fun getOrdersByClient(clientId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderDate BETWEEN :startDate AND :endDate ORDER BY orderDate DESC")
    fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderDate DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<OrderEntity>>

    @Transaction
    suspend fun insertOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>) {
        insertOrder(order)
        insertOrderItems(items)
    }

    @Transaction
    suspend fun deleteOrderWithItems(order: OrderEntity) {
        deleteOrderItems(order.id)
        deleteOrder(order)
    }

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)

    @Query("UPDATE orders SET deliveryDate = :deliveryDate WHERE id = :orderId")
    suspend fun updateDeliveryDate(orderId: String, deliveryDate: Date)
} 