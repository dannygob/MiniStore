package com.example.ministore.domain.model

import com.example.minstore.domain.models.OrderStatus
import com.google.mlkit.vision.barcode.common.Barcode.Address
import java.util.Date
import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val clientId: String,
    val products: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val deliveryAddress: Address,
    val orderDate: Date = Date(),
    val deliveryDate: Date? = null,
    val notes: String? = null,
    val trackingNumber: String? = null
)

data class OrderItem(
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double = quantity * unitPrice,
    val notes: String? = null
) 