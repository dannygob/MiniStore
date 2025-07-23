package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.OrderDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.data.domain.repository.OrderRepository
import com.example.ministore.domain.model.Order
import com.example.ministore.domain.model.OrderItem
import com.example.minstore.domain.models.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class OrderRepositoryImpl(private val orderDao: OrderDao) : OrderRepository {

    override suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>) {
        orderDao.insertOrderWithItems(order.toEntity(), items.map { it.toEntity(order.id) })
    }

    override suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order.toEntity())
    }

    override suspend fun deleteOrderWithItems(order: Order) {
        orderDao.deleteOrderWithItems(order.toEntity())
    }

    override fun getOrderById(id: String): Flow<Order?> {
        return orderDao.getOrderById(id).map { it?.toDomain() }
    }

    override fun getOrderItemsByOrderId(orderId: String): Flow<List<OrderItem>> {
        return orderDao.getOrderItemsByOrderId(orderId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getOrdersByClient(clientId: String): Flow<List<Order>> {
        return orderDao.getOrdersByClient(clientId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<Order>> {
        return orderDao.getOrdersByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> {
        return orderDao.getOrdersByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        orderDao.updateOrderStatus(orderId, status)
    }

    override suspend fun updateDeliveryDate(orderId: String, deliveryDate: Date) {
        orderDao.updateDeliveryDate(orderId, deliveryDate)
    }
}