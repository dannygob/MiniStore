package com.example.minstore.domain.models

import java.util.Date

data class Sale(
    val id: String = "",
    val clientId: String?,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val items: List<SaleDetail>,
    val status: SaleStatus = SaleStatus.COMPLETED,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class SaleDetail(
    val id: String = "",
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

enum class PaymentMethod {
    CASH,
    CARD,
    TRANSFER
}

enum class SaleStatus {
    PENDING,
    COMPLETED,
    CANCELLED
} 