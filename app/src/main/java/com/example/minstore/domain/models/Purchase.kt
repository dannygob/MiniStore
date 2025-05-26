package com.example.minstore.domain.models

import java.util.Date

data class Purchase(
    val id: String = "",
    val providerId: String,
    val total: Double,
    val items: List<PurchaseDetail>,
    val status: PurchaseStatus = PurchaseStatus.COMPLETED,
    val paymentMethod: PaymentMethod,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class PurchaseDetail(
    val id: String = "",
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

enum class PurchaseStatus {
    PENDING,
    COMPLETED,
    CANCELLED
} 