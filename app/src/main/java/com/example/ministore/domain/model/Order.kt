package com.example.ministore.domain.model

import com.example.ministore.domain.model.OrderItem
import com.example.ministore.domain.model.PaymentMethod
import com.example.ministore.domain.model.OrderStatus
import com.example.ministore.domain.model.Address
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
