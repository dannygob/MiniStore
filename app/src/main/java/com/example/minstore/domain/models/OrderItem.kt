package com.example.minstore.domain.models

data class OrderItem(
    val id: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)