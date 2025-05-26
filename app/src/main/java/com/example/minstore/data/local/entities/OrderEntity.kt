package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.minstore.domain.models.OrderStatus
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val clientId: String,
    val total: Double,
    val status: OrderStatus,
    val deliveryAddressStreet: String?,
    val deliveryAddressNumber: String?,
    val deliveryAddressCity: String?,
    val deliveryAddressState: String?,
    val deliveryAddressZipCode: String?,
    val deliveryDate: Date?,
    val notes: String?,
    val createdAt: Date,
    val updatedAt: Date
)

@Entity(
    tableName = "order_details",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderDetailEntity(
    @PrimaryKey
    val id: String,
    val orderId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
) 