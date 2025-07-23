package com.example.ministore.domain.model

data class PurchaseItem(
    val id: Long = 0,
    val purchaseId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)