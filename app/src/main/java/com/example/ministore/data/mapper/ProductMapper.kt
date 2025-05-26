package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.ProductEntity
import com.example.ministore.domain.model.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        barcode = barcode,
        price = price,
        cost = cost,
        stock = stock,
        minStock = minStock,
        category = category,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        barcode = barcode,
        price = price,
        cost = cost,
        stock = stock,
        minStock = minStock,
        category = category,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 