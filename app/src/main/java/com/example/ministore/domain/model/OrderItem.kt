package com.example.ministore.domain.model

data class OrderItem(
    val id: Long = 0,
    val orderId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val notes: String?
)