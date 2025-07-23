package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.OrderEntity
import com.example.ministore.data.local.entity.OrderItemEntity
import com.example.ministore.domain.model.Order
import com.example.ministore.domain.model.OrderItem

fun OrderEntity.toDomain(): Order {
    return Order(
        id = id,
        clientId = clientId,
        totalAmount = totalAmount,
        status = status,
        paymentMethod = paymentMethod,
        deliveryAddress = deliveryAddress,
        orderDate = orderDate,
        deliveryDate = deliveryDate,
        notes = notes,
        trackingNumber = trackingNumber
    )
}

fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        id = id,
        orderId = orderId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal,
        notes = notes
    )
}

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        clientId = clientId,
        totalAmount = totalAmount,
        status = status,
        paymentMethod = paymentMethod,
        deliveryAddress = deliveryAddress,
        orderDate = orderDate,
        deliveryDate = deliveryDate,
        notes = notes,
        trackingNumber = trackingNumber
    )
}

fun OrderItem.toEntity(orderId: String): OrderItemEntity {
    return OrderItemEntity(
        orderId = orderId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal,
        notes = notes
    )
}