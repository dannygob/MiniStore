package com.example.ministore.domain.model

data class SaleItem(
    val saleId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discount: Double
)