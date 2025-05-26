package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.OrderDao
import com.example.ministore.data.local.entity.OrderEntity
import com.example.ministore.data.local.entity.OrderItemEntity
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Order
import com.example.ministore.domain.model.OrderItem
import com.example.ministore.domain.model.OrderStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore
) {
    private val ordersCollection = firestore.collection("orders")
    private val orderItemsCollection = firestore.collection("order_items")

    suspend fun insertOrder(order: Order, items: List<OrderItem>) {
        val orderEntity = order.toEntity()
        val itemEntities = items.map { it.toEntity(order.id) }

        // Save to local database
        orderDao.insertOrderWithItems(orderEntity, itemEntities)

        // Save to Firestore
        ordersCollection.document(order.id).set(order).await()
        items.forEach { item ->
            orderItemsCollection.document().set(item).await()
        }
    }

    suspend fun updateOrder(order: Order) {
        val orderEntity = order.toEntity()

        // Update local database
        orderDao.updateOrder(orderEntity)

        // Update Firestore
        ordersCollection.document(order.id).set(order).await()
    }

    suspend fun deleteOrder(order: Order) {
        val orderEntity = order.toEntity()

        // Delete from local database
        orderDao.deleteOrderWithItems(orderEntity)

        // Delete from Firestore
        ordersCollection.document(order.id).delete().await()
        orderItemsCollection
            .whereEqualTo("orderId", order.id)
            .get()
            .await()
            .documents
            .forEach { doc -> doc.reference.delete().await() }
    }

    fun getOrderById(id: String): Flow<Order?> {
        return orderDao.getOrderById(id).map { orderEntity ->
            orderEntity?.let { entity ->
                val items = orderDao.getOrderItemsByOrderId(id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                entity.toDomain(items)
            }
        }
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { orders ->
            orders.map { order ->
                val items = orderDao.getOrderItemsByOrderId(order.id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                order.toDomain(items)
            }
        }
    }

    fun getOrdersByClient(clientId: String): Flow<List<Order>> {
        return orderDao.getOrdersByClient(clientId).map { orders ->
            orders.map { order ->
                val items = orderDao.getOrderItemsByOrderId(order.id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                order.toDomain(items)
            }
        }
    }

    fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<Order>> {
        return orderDao.getOrdersByDateRange(startDate, endDate).map { orders ->
            orders.map { order ->
                val items = orderDao.getOrderItemsByOrderId(order.id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                order.toDomain(items)
            }
        }
    }

    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> {
        return orderDao.getOrdersByStatus(status).map { orders ->
            orders.map { order ->
                val items = orderDao.getOrderItemsByOrderId(order.id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                order.toDomain(items)
            }
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        // Update local database
        orderDao.updateOrderStatus(orderId, status)

        // Update Firestore
        ordersCollection.document(orderId)
            .update("status", status)
            .await()
    }

    suspend fun updateDeliveryDate(orderId: String, deliveryDate: Date) {
        // Update local database
        orderDao.updateDeliveryDate(orderId, deliveryDate)

        // Update Firestore
        ordersCollection.document(orderId)
            .update("deliveryDate", deliveryDate)
            .await()
    }

    suspend fun syncWithFirestore() {
        // Fetch all orders from Firestore
        val firestoreOrders = ordersCollection.get().await()

        firestoreOrders.documents.forEach { doc ->
            val order = doc.toObject(Order::class.java)
            order?.let {
                val items = orderItemsCollection
                    .whereEqualTo("orderId", it.id)
                    .get()
                    .await()
                    .toObjects(OrderItem::class.java)

                // Update local database
                orderDao.insertOrderWithItems(
                    it.toEntity(),
                    items.map { item -> item.toEntity(it.id) }
                )
            }
        }
    }
} 