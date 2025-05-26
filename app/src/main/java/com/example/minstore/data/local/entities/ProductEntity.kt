package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val barcode: String?,
    val purchasePrice: Double,
    val sellingPrice: Double,
    val category: String,
    val stock: Int,
    val providerId: String,
    val locationAisle: String,
    val locationShelf: String,
    val locationLevel: String,
    val minimumStock: Int,
    val expirationDate: Date?,
    val imageUrl: String?,
    val createdAt: Date,
    val updatedAt: Date
) 