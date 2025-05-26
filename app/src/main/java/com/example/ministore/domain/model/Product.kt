package com.example.ministore.domain.model

import java.util.Date

data class Product(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val barcode: String? = null,
    val price: Double,
    val cost: Double,
    val stock: Int,
    val minStock: Int,
    val category: String? = null,
    val imageUrl: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    val profit: Double
        get() = price - cost

    val profitMargin: Double
        get() = if (cost > 0) (profit / cost) * 100 else 0.0

    val isLowStock: Boolean
        get() = stock <= minStock
} 