package com.example.ministore.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ministore.data.local.converter.DateConverter
import com.example.ministore.data.local.converter.OrderStatusConverter
import com.example.ministore.data.local.converter.PaymentMethodConverter
import com.example.ministore.domain.model.Address
import com.example.ministore.domain.model.OrderStatus
import com.example.ministore.domain.model.PaymentMethod
import java.util.Date

@Entity(tableName = "orders")
@TypeConverters(DateConverter::class, OrderStatusConverter::class, PaymentMethodConverter::class)
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val clientId: String,
    val totalAmount: Double,
    val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    @Embedded(prefix = "delivery_")
    val deliveryAddress: Address,
    val orderDate: Date,
    val deliveryDate: Date?,
    val notes: String?,
    val trackingNumber: String?
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val notes: String?
) 