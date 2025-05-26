package com.example.minstore.domain.models

import java.util.Date

data class Product(
    val id: String = "",
    val name: String,
    val barcode: String?,
    val purchasePrice: Double,
    val sellingPrice: Double,
    val category: String,
    val stock: Int,
    val providerId: String,
    val location: ProductLocation,
    val minimumStock: Int,
    val expirationDate: Date?,
    val imageUrl: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class ProductLocation(
    val aisle: String,
    val shelf: String,
    val level: String
) 