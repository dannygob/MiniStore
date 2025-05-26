package com.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ministore.data.local.converter.DateTimeConverter
import com.ministore.data.local.converter.StringListConverter
import java.math.BigDecimal
import java.time.LocalDateTime
import com.ministore.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val category: String,
    val imageUrl: String?,
    val lastModified: Long = System.currentTimeMillis()
) {
    fun toProduct(): Product = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        quantity = quantity,
        category = category,
        imageUrl = imageUrl
    )

    companion object {
        fun fromProduct(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            quantity = product.quantity,
            category = product.category,
            imageUrl = product.imageUrl
        )
    }
} 