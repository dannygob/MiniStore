package com.example.ministore.domain.model

import java.util.Date
import java.util.UUID

data class Purchase(
    val id: String = UUID.randomUUID().toString(),
    val providerId: String,
    val products: List<PurchaseItem>,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val status: PurchaseStatus,
    val date: Date = Date(),
    val notes: String? = null,
    val invoiceNumber: String? = null,
    val receiptUrl: String? = null
)

data class PurchaseItem(
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double = quantity * unitPrice
) 