package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.PurchaseEntity
import com.example.ministore.data.local.entity.PurchaseItemEntity
import com.example.ministore.domain.model.Purchase
import com.example.ministore.domain.model.PurchaseItem

fun PurchaseEntity.toDomain(): Purchase {
    return Purchase(
        id = id,
        providerId = providerId,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        status = status,
        date = date,
        notes = notes,
        invoiceNumber = invoiceNumber,
        receiptUrl = receiptUrl
    )
}

fun PurchaseItemEntity.toDomain(): PurchaseItem {
    return PurchaseItem(
        id = id,
        purchaseId = purchaseId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal
    )
}

fun Purchase.toEntity(): PurchaseEntity {
    return PurchaseEntity(
        id = id,
        providerId = providerId,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        status = status,
        date = date,
        notes = notes,
        invoiceNumber = invoiceNumber,
        receiptUrl = receiptUrl
    )
}

fun PurchaseItem.toEntity(purchaseId: String): PurchaseItemEntity {
    return PurchaseItemEntity(
        purchaseId = purchaseId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal
    )
}