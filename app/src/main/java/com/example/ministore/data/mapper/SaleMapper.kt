package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.SaleEntity
import com.example.ministore.data.local.entity.SaleItemEntity
import com.example.ministore.data.local.entity.SaleWithItems
import com.example.ministore.domain.model.Sale
import com.example.ministore.domain.model.SaleItem

fun SaleWithItems.toDomain(): Sale {
    return Sale(
        id = sale.id,
        clientId = sale.clientId,
        total = sale.total,
        items = items.map { it.toDomain() },
        paymentMethod = sale.paymentMethod,
        status = sale.status,
        createdAt = sale.createdAt,
        updatedAt = sale.updatedAt
    )
}

fun Sale.toEntity(): SaleEntity {
    return SaleEntity(
        id = id,
        clientId = clientId,
        total = total,
        paymentMethod = paymentMethod,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun SaleItemEntity.toDomain(): SaleItem {
    return SaleItem(
        productId = productId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice,
        discount = discount
    )
}

fun SaleItem.toEntity(saleId: Long): SaleItemEntity {
    return SaleItemEntity(
        saleId = saleId,
        productId = productId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice,
        discount = discount
    )
} 