package com.example.ministore.domain.model

import java.util.Date

data class Sale(
    val id: Long = 0,
    val clientId: Long? = null,
    val total: Double,
    val items: List<SaleItem>,
    val paymentMethod: PaymentMethod,
    val status: SaleStatus = SaleStatus.COMPLETED,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    val itemCount: Int
        get() = items.sumOf { it.quantity }

    val totalItems: Int
        get() = items.size
}

data class SaleItem(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discount: Double = 0.0
) {
    val total: Double
        get() = quantity * unitPrice * (1 - discount)
}

enum class PaymentMethod {
    CASH, CREDIT_CARD, DEBIT_CARD, TRANSFER, OTHER
}

enum class SaleStatus {
    PENDING, COMPLETED, CANCELLED
} 