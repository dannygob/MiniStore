package com.ministore.domain.model

data class Sale(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val pricePerUnit: Double = 0.0,
    val totalAmount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun calculateTotal(): Double = quantity * pricePerUnit
} 