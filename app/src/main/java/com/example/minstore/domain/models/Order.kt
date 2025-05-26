package com.example.minstore.domain.models

import java.util.Date

data class Order(
    val id: String = "",
    val clientId: String,
    val items: List<OrderItem>,
    val total: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val deliveryAddress: Address?,
    val deliveryDate: Date?,
    val notes: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class OrderItem(
    val id: String = "",
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

enum class OrderStatus {
    PENDING,
    IN_PROGRESS,
    READY,
    DELIVERED,
    CANCELLED
} 