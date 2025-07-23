package com.example.minstore.domain.models

data class PurchaseItem(
    val id: String,
    val productId: String,
    val quantity: Int,
    val unitCost: Double,
    val subtotal: Double
)