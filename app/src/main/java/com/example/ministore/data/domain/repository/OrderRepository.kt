package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Order
import com.example.ministore.domain.model.OrderItem
import com.example.minstore.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface OrderRepository {
    suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>)
    suspend fun updateOrder(order: Order)
    suspend fun deleteOrderWithItems(order: Order)
    fun getOrderById(id: String): Flow<Order?>
    fun getOrderItemsByOrderId(orderId: String): Flow<List<OrderItem>>
    fun getAllOrders(): Flow<List<Order>>
    fun getOrdersByClient(clientId: String): Flow<List<Order>>
    fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<Order>>
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)
    suspend fun updateDeliveryDate(orderId: String, deliveryDate: Date)
}