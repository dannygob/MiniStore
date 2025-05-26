package com.example.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val barcode: String?,
    val price: Double,
    val cost: Double,
    val stock: Int,
    val minStock: Int,
    val category: String?,
    val imageUrl: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isDeleted: Boolean = false
) 